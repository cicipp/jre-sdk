package co.buybuddy.networking.authentication.artifacts.concrete;

import co.buybuddy.networking.authentication.persistence.SecurePersistenceManager;
import co.buybuddy.networking.authentication.persistence.SecureType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Passphrase {
    private static final int PASSPHRASE_LENGTH = 192;

    private long userId;
    private String passkey;
    private Date createdAt;

    public Passphrase(long userId, String passkey, Date createdAt) {
        this.userId = userId;
        this.passkey = passkey;
        this.createdAt = createdAt;

        if (passkey.length() != PASSPHRASE_LENGTH) {
            throw new IllegalArgumentException("passkey has unexpected length");
        }
    }

    void persist(SecurePersistenceManager manager, String key) throws IOException {
        //  TODO: Should also include user identifier.
        manager.persistData(
                passkey.getBytes(StandardCharsets.UTF_8),
                SecureType.CRYPTOGRAPHIC_KEY,
                key);
    }

    public long getUserId() {
        return userId;
    }

    public String getPasskey() {
        return passkey;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
