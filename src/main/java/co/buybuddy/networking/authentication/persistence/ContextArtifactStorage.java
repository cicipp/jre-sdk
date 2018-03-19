package co.buybuddy.networking.authentication.persistence;

import java.io.IOException;

public interface ContextArtifactStorage {
    String toUriSafeEncodedString() throws IOException;
}
