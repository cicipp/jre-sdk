package co.buybuddy;

import co.buybuddy.networking.authentication.artifacts.concrete.ConcreteAuthenticationException;
import co.buybuddy.networking.authentication.artifacts.concrete.Passphrase;
import co.buybuddy.networking.authentication.artifacts.concrete.TwoFactorAuthenticationException;
import co.buybuddy.networking.authentication.persistence.ContextStealthArtifactStorage;
import co.buybuddy.networking.authentication.persistence.ContextPassphraseRepository;
import co.buybuddy.networking.authentication.primitives.Credentials;
import co.buybuddy.networking.authentication.primitives.OneTimeCode;
import co.buybuddy.networking.authentication.tfa.ContextProcessObserver;
import co.buybuddy.networking.authentication.tfa.ContextProcessType;
import co.buybuddy.networking.errorhandling.InternalInconsistencyException;
import co.buybuddy.networking.errorhandling.NetworkIOException;
import co.buybuddy.networking.errorhandling.PlatformRPCException;
import co.buybuddy.networking.http.HttpClient;
import co.buybuddy.networking.http.HttpClientCallDelegate;
import co.buybuddy.networking.http.HttpClientFactory;
import co.buybuddy.networking.http.RequestBuilder;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.util.Date;

import static co.buybuddy.networking.authentication.tfa.ContextProcessType.INSECURE;
import static co.buybuddy.networking.authentication.tfa.ContextProcessType.ONE_TIME_CODE_OVER_EMAIL;
import static co.buybuddy.networking.authentication.tfa.ContextProcessType.ONE_TIME_CODE_OVER_EXTERNAL_AUTHENTICATOR;
import static co.buybuddy.networking.authentication.tfa.ContextProcessType.ONE_TIME_CODE_OVER_SMS;

/**
 * Represents the connection context to the platform.
 * Authenticates client with the platform with given authentication credentials.
 */
public class Context {
    /**
     * Represents the state of the connection context.
     * If state is `AWAITING`, it means that context is currently awaiting to authenticate.
     * If state is `OPEN`, it means that context is successfully authenticated and waiting for operations.
     */
    public enum State {
        AWAITING,
        OPEN,
        CLOSED,
    }

    private class Authenticator {
        private class SignInForm {
            private Credentials credentials;

            SignInForm(Credentials credentials) {
                this.credentials = credentials;
            }
        }

        private class OneTimeCodeForm {
            @JsonProperty("user_id")
            long userId;

            @JsonIgnore
            OneTimeCode oneTimeCode;

            public OneTimeCodeForm(long userId, OneTimeCode oneTimeCode) {
                this.userId = userId;
                this.oneTimeCode = oneTimeCode;
            }

            @JsonGetter("one_time_code")
            String getOneTimeCodeString() {
                return oneTimeCode.getCode();
            }
        }

        private class SignInResponseContinue {
            private class ContextProcessTypeDeserializer extends StdDeserializer<ContextProcessType> {
                private static final String INSECURE_SCHEME = "insecure";
                private static final String PRE_OTC_OVER_SMS_SCHEME = "pre_otc_over_sms";
                private static final String PRE_OTC_OVER_EMAIL_SCHEME = "pre_otc_over_email";
                private static final String PRE_OTC_OVER_GUARD_SCHEME = "pre_otc_over_guard";

                public ContextProcessTypeDeserializer(Class<?> vc) {
                    super(vc);
                }

                @Override
                public ContextProcessType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, InternalInconsistencyException {
                    try {
                        String type = jsonParser.getText();

                        switch (type) {
                            case INSECURE_SCHEME:
                                return INSECURE;
                            case PRE_OTC_OVER_SMS_SCHEME:
                                return ONE_TIME_CODE_OVER_SMS;
                            case PRE_OTC_OVER_EMAIL_SCHEME:
                                return ONE_TIME_CODE_OVER_EMAIL;
                            case PRE_OTC_OVER_GUARD_SCHEME:
                                return ONE_TIME_CODE_OVER_EXTERNAL_AUTHENTICATOR;
                        }

                        throw new InternalInconsistencyException("unexpected authentication scheme: " + type);
                    } catch (JsonProcessingException jpe) {
                        throw new InternalInconsistencyException("cannot deserialize response", jpe);
                    }
                }
            }

            @JsonProperty("user_id")
            long userId;

            @JsonDeserialize(using = ContextProcessTypeDeserializer.class)
            ContextProcessType type;
        }

        private class SignInResponseSuccessful extends SignInResponseContinue {
            @JsonProperty("passphrase")
            String passkey;
        }

        private class SignInResult {
            private boolean completed;
            private ContextProcessType type;
            private long userId;
            private Passphrase passphrase;

            SignInResult(boolean completed, ContextProcessType type, long userId, Passphrase passphrase) {
                this.completed = completed;
                this.type = type;
                this.userId = userId;
                this.passphrase = passphrase;
            }

            SignInResult(boolean completed, ContextProcessType type, long userId) {
                this.completed = completed;
                this.type = type;
                this.userId = userId;
            }

            long getUserId() {
                return userId;
            }

            ContextProcessType getType() {
                return type;
            }

            Passphrase getPassphrase() {
                return passphrase;
            }

            boolean isCompleted() {
                return completed;
            }
        }

        private HttpClient client;

        Authenticator(HttpClient client) {
            this.client = client;
        }

        SignInResult signIn(Credentials credentials) throws ConcreteAuthenticationException, NetworkIOException {
            SignInForm form = new SignInForm(credentials);
            Request request = null;

            try {
                request = new RequestBuilder()
                        .operation("signIn", null)
                        .serialize(form)
                        .build();
            } catch (JsonProcessingException exc) {
                throw new InternalInconsistencyException("error thrown while serialization", exc);
            }

            SignInResult result = client.executeCall(request, new HttpClientCallDelegate<SignInResult>() {
                @Override
                public SignInResult onContinue(Response response) throws IOException {
                    ObjectMapper mapper = new ObjectMapper();
                    SignInResponseContinue continueResponse = mapper.readValue(response.body().byteStream(), SignInResponseContinue.class);

                    return new SignInResult(false, continueResponse.type, continueResponse.userId);
                }

                @Override
                public SignInResult onCreated(Response response) throws IOException {
                    ObjectMapper mapper = new ObjectMapper();
                    SignInResponseSuccessful successfulResponse = mapper.readValue(response.body().byteStream(), SignInResponseSuccessful.class);

                    return new SignInResult(true, successfulResponse.type, successfulResponse.userId, new Passphrase(successfulResponse.userId, successfulResponse.passkey, new Date()));
                }

                @Override
                public SignInResult onNotFound(Response response) throws IOException {
                    return null;
                }
            });

            if (result == null) {
                throw new ConcreteAuthenticationException(credentials);
            }

            return result;
        }

        Passphrase completeSignIn(SignInResult previousResult, OneTimeCode oneTimeCode) throws TwoFactorAuthenticationException, NetworkIOException {
            OneTimeCodeForm form = new OneTimeCodeForm(previousResult.getUserId(), oneTimeCode);
            Request request = null;

            try {
                request = new RequestBuilder()
                        .operation("completeSignIn", null)
                        .serialize(form)
                        .build();
            } catch (JsonProcessingException e) {
                throw new InternalInconsistencyException("cannot marshall object", e);
            }

            Passphrase result = client.executeCall(request, new HttpClientCallDelegate<Passphrase>() {
                @Override
                public Passphrase onContinue(Response response) throws IOException {
                    ObjectMapper mapper = new ObjectMapper();

                    SignInResponseSuccessful successfulResponse = mapper.readValue(response.body().byteStream(), SignInResponseSuccessful.class);

                    return new Passphrase(successfulResponse.userId, successfulResponse.passkey, new Date());
                }

                @Override
                public Passphrase onNotFound(Response response) throws IOException {
                    return null;
                }
            });

            if (result == null) {
                throw new TwoFactorAuthenticationException(oneTimeCode);
            }

            return result;
        }
    }

    private ContextStealthArtifactStorage artifactStorage;
    private ContextPassphraseRepository passphraseRepository;
    private State currentState;
    private HttpClient client;
    private Authenticator authenticator;

    public Context(ContextStealthArtifactStorage artifactStorage, ContextPassphraseRepository passphraseRepository) {
        Validate.notNull(artifactStorage);
        Validate.notNull(passphraseRepository);

        this.artifactStorage = artifactStorage;
        this.passphraseRepository = passphraseRepository;
        this.currentState = State.AWAITING;
    }

    /**
     * Opens the context with given credentials.
     * @param credentials Credentials to be used in authentication.
     * @param observer An observer instance to survive from various authentication schemes.
     */
    public void open(Credentials credentials, ContextProcessObserver observer) throws ConcreteAuthenticationException, TwoFactorAuthenticationException, IOException {
        Validate.notNull(credentials);
        Validate.notNull(observer);

        Authenticator.SignInResult result = getAuthenticator().signIn(credentials);

        if (result.isCompleted()) {
            finalize(result, observer);
        } else {
            Passphrase passphrase = null;

            switch (result.getType()) {
                case ONE_TIME_CODE_OVER_SMS:
                    passphrase = getAuthenticator().completeSignIn(result, observer.requiresSmsAuthentication());
                    break;

                case ONE_TIME_CODE_OVER_EMAIL:
                    passphrase = getAuthenticator().completeSignIn(result, observer.requiresEmailAuthentication());
                    break;

                case ONE_TIME_CODE_OVER_EXTERNAL_AUTHENTICATOR:
                    passphrase = getAuthenticator().completeSignIn(result, observer.requiresExternalAuthentication());
                    break;

                default:
                    throw new InternalInconsistencyException("sign in result type should require forward operation flow");
            }

            finalize(getAuthenticator().new SignInResult(true, result.type, result.userId, passphrase), observer);
        }

        remake(result.getPassphrase());
    }

    private void finalize(Authenticator.SignInResult result, ContextProcessObserver observer) throws IOException {
        passphraseRepository.store(result.getPassphrase());

        observer.didOpen();
    }

    private void remake(Passphrase passphrase) {


        currentState = State.OPEN;
    }

    public void remake() throws IOException {
        remake(passphraseRepository.retrieve());
    }

    public ContextStealthArtifactStorage getArtifactStorage() {
        return artifactStorage;
    }

    public ContextPassphraseRepository getPassphraseRepository() {
        return passphraseRepository;
    }

    public State getCurrentState() {
        return currentState;
    }

    private Authenticator getAuthenticator() {
        if (authenticator == null) {
            authenticator = new Authenticator(getClient());
        }

        return authenticator;
    }

    private HttpClient getClient() {
        if (client == null) {
            this.client = new HttpClientFactory().getClient();
        }

        return client;
    }
}