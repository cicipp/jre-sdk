package co.buybuddy.networking.authentication.artifacts.concrete;

import co.buybuddy.networking.authentication.primitives.Credentials;

public class ConcreteAuthenticationException extends Exception {
    private Credentials credentials;

    public ConcreteAuthenticationException(Credentials credentials) {
        super("authenticity of given credentials could not be approved from the platform for user identifier - " + credentials.getIdentifier());

        this.credentials = credentials;
    }
}
