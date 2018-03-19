package co.buybuddy.networking.authentication.persistence;

import co.buybuddy.networking.authentication.artifacts.stealth.Token;

import java.io.IOException;

/**
 * Stores a context artifact in memory.
 */
public class ContextArtifactInMemoryStorage implements ContextArtifactStorage {
    private Token token;

    public ContextArtifactInMemoryStorage(Token passphrase) {
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
