package co.buybuddy.networking.authentication.persistence;

import co.buybuddy.networking.authentication.artifacts.stealth.Token;

import java.io.IOException;

/**
 * Stores a context artifact in memory.
 */
public class ContextStealthArtifactInMemoryStorage implements ContextStealthArtifactStorage {
    private Token token;

    public ContextStealthArtifactInMemoryStorage(Token passphrase) {
        this.token = token;
    }

    @Override
    public String toUriSafeEncodedString() throws IOException {
        return getToken().toString();
    }

    public Token getToken() {
        return token;
    }
}
