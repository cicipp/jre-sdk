package com.buybuddy.networking.authentication.primitives;

import com.buybuddy.networking.authentication.persistence.SecurePersistenceManager;
import com.buybuddy.networking.authentication.persistence.SecureType;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Passphrase {
    private String passkey;
    private Date createdAt;

    void persistWithManager(SecurePersistenceManager manager, String key) {
        manager.persistData(
                passkey.getBytes(StandardCharsets.UTF_8),
                SecureType.CRYPTOGRAPHIC_KEY,
                key);
    }
}
