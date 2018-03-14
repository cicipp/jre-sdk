package com.buybuddy.networking.authentication.primitives;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Credentials {
    /**
     * An identifier of a platform user, consisting email or username.
     */
    @Getter
    private String identifier;

    /**
     * Password of the platform user with given identifier.
     */
    @Getter
    private String password;
}
