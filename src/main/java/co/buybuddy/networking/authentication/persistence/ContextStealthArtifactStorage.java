package co.buybuddy.networking.authentication.persistence;

import java.io.IOException;

public interface ContextStealthArtifactStorage {
    String toUriSafeEncodedString() throws IOException;
}
