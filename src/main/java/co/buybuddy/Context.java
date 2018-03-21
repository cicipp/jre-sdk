package co.buybuddy;

import co.buybuddy.networking.authentication.ContextResolutionError;
import co.buybuddy.networking.authentication.artifacts.concrete.ConcreteAuthenticationException;
import co.buybuddy.networking.authentication.artifacts.concrete.Passphrase;
import co.buybuddy.networking.authentication.artifacts.concrete.TwoFactorAuthenticationException;
import co.buybuddy.networking.authentication.persistence.ContextStealthArtifactStorage;
import co.buybuddy.networking.authentication.persistence.ContextPassphraseRepository;
import co.buybuddy.networking.authentication.primitives.Credentials;
import co.buybuddy.networking.authentication.primitives.OneTimeCode;
import co.buybuddy.networking.authentication.tfa.ContextProcessObserver;
import co.buybuddy.networking.authentication.tfa.ContextProcessType;
import co.buybuddy.networking.http.HttpClient;
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

            public SignInForm(Credentials credentials) {
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
                public ContextProcessType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
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

                    throw new InternalError("unexpected authentication scheme: " + type);
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

        public class SignInResult {
            private boolean completed;
            private ContextProcessType type;
            private long userId;
            private Passphrase passphrase;

            public SignInResult(boolean completed, ContextProcessType type, long userId, Passphrase passphrase) {
                this.completed = completed;
                this.type = type;
                this.userId = userId;
                this.passphrase = passphrase;
            }

            public SignInResult(boolean completed, ContextProcessType type, long userId) {
                this.completed = completed;
                this.type = type;
                this.userId = userId;
            }

            public long getUserId() {
                return userId;
            }

            public ContextProcessType getType() {
                return type;
            }

            public Passphrase getPassphrase() {
                return passphrase;
            }

            public boolean isCompleted() {
                return completed;
            }
        }

        private HttpClient client;

        public Authenticator(HttpClient client) {
            this.client = client;
        }

        public SignInResult signIn(Credentials credentials) throws IOException, ConcreteAuthenticationException {
            SignInForm form = new SignInForm(credentials);

            try {
                Request request = new RequestBuilder()
                        .operation("signIn", null)
                        .serialize(form)
                        .build();

                Response response = client.newCall(request).execute();
                ObjectMapper mapper = new ObjectMapper();

                if (response.code() == 100) {
                    SignInResponseContinue continueResponse = mapper.readValue(response.body().byteStream(), SignInResponseContinue.class);

                    return new SignInResult(false, continueResponse.type, continueResponse.userId);
                } else if (response.code() == 201) {
                    SignInResponseSuccessful successfulResponse = mapper.readValue(response.body().byteStream(), SignInResponseSuccessful.class);

                    return new SignInResult(true, successfulResponse.type, successfulResponse.userId, new Passphrase(successfulResponse.userId, successfulResponse.passkey, new Date()));
                } else if (response.code() == 404) {
                    throw new ConcreteAuthenticationException(credentials);
                } else {
                    throw new InternalError("could not deserialize server response");
                }
            } catch (JsonProcessingException exc) {
                throw new InternalError("error thrown while serialization", exc);
            }
        }

        public SignInResult completeSignIn(SignInResult result, OneTimeCode oneTimeCode) throws IOException, ConcreteAuthenticationException, TwoFactorAuthenticationException {
            OneTimeCodeForm form = new OneTimeCodeForm(result.getUserId(), oneTimeCode);

            try {
                Request request = new RequestBuilder()
                        .operation("completeSignIn", null)
                        .serialize(form)
                        .build();

                Response response = client.newCall(request).execute();
                ObjectMapper mapper = new ObjectMapper();

                if (response.code() == 201) {
                    SignInResponseSuccessful successfulResponse = mapper.readValue(response.body().byteStream(), SignInResponseSuccessful.class);

                    return new SignInResult(true, successfulResponse.type, successfulResponse.userId, new Passphrase(successfulResponse.userId, successfulResponse.passkey, new Date()));
                } else if (response.code() == 404) {
                    throw new TwoFactorAuthenticationException(oneTimeCode);
                } else {
                    throw new InternalError("could not deserialize server response");
                }
            } catch (JsonProcessingException exc) {
                throw new InternalError("error thrown while serialization", exc);
            }
        }
    }

    public static final String LIBRARY_VERSION = "0.0.0";
    public static final String LIBRARY_NAME = "BuyBuddyJavaSDK";

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
    public void open(Credentials credentials, ContextProcessObserver observer) throws IOException, ConcreteAuthenticationException, TwoFactorAuthenticationException {
        Validate.notNull(credentials);
        Validate.notNull(observer);

        Authenticator.SignInResult result = getAuthenticator().signIn(credentials);

        if (result.isCompleted()) {
            finalize(result, observer);
        } else {
            switch (result.getType()) {
                case ONE_TIME_CODE_OVER_SMS:
                    result = getAuthenticator().completeSignIn(result, observer.requiresSmsAuthentication());
                    break;

                case ONE_TIME_CODE_OVER_EMAIL:
                    result = getAuthenticator().completeSignIn(result, observer.requiresEmailAuthentication());
                    break;

                case ONE_TIME_CODE_OVER_EXTERNAL_AUTHENTICATOR:
                    result = getAuthenticator().completeSignIn(result, observer.requiresExternalAuthentication());
                    break;

                default:
                    throw new InternalError("sign in result type should require forward operation flow");
            }

            finalize(result, observer);
        }

        remake(result.getPassphrase());
    }

    private void finalize(Authenticator.SignInResult result, ContextProcessObserver observer) throws IOException {
        passphraseRepository.store(result.getPassphrase());

        observer.didOpen();
    }

    private void remake(Passphrase passphrase) throws IOException {


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