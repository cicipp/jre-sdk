package co.buybuddy.networking.authentication.persistence;

import co.buybuddy.networking.authentication.artifacts.concrete.Passphrase;

import java.io.IOException;

public interface ContextPassphraseRepository {
    public Passphrase retrieve() throws IOException;
}
