package co.buybuddy;

import co.buybuddy.networking.authentication.artifacts.concrete.ConcreteAuthenticationException;
import co.buybuddy.networking.authentication.artifacts.concrete.Passphrase;
import co.buybuddy.networking.authentication.persistence.ContextArtifactStorage;
import co.buybuddy.networking.authentication.persistence.ContextPassphraseRepository;
import co.buybuddy.networking.authentication.primitives.Credentials;
import co.buybuddy.networking.authentication.tfa.ContextProcessObserver;
import org.apache.commons.lang3.Validate;

import java.io.IOException;

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

    public static final String LIBRARY_VERSION = "0.0.0";
    public static final String LIBRARY_NAME = "BuyBuddyJavaSDK";

    private ContextArtifactStorage artifactStorage;
    private ContextPassphraseRepository passphraseRepository;
    private State currentState;

    public Context(ContextArtifactStorage artifactStorage, ContextPassphraseRepository passphraseRepository) {
        Validate.notNull(artifactStorage);

        this.artifactStorage = artifactStorage;
        this.passphraseRepository = passphraseRepository;
        this.currentState = State.AWAITING;
    }

    /**
     * Opens the context with given credentials.
     * @param credentials Credentials to be used in authentication.
     * @param observer An observer instance to survive from various authentication schemes.
     */
    public void open(Credentials credentials, ContextProcessObserver observer) throws ConcreteAuthenticationException {
        Validate.notNull(credentials);
        Validate.notNull(observer);

        currentState = State.OPEN;
    }

    public void remake() throws IOException {
        Validate.notNull(passphraseRepository);

        Passphrase passphrase = passphraseRepository.retrieve();

        currentState = State.OPEN;
    }

    public ContextArtifactStorage getArtifactStorage() {
        return artifactStorage;
    }

    public ContextPassphraseRepository getPassphraseRepository() {
        return passphraseRepository;
    }

    public State getCurrentState() {
        return currentState;
    }
}