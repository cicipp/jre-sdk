package co.buybuddy.networking.authentication.artifacts.concrete;

import co.buybuddy.networking.authentication.primitives.OneTimeCode;

public class TwoFactorAuthenticationException extends Exception {
    private OneTimeCode oneTimeCode;

    public TwoFactorAuthenticationException(OneTimeCode oneTimeCode) {
        super("two factor authentication failed due to submission of non-matching one time code");

        this.oneTimeCode = oneTimeCode;
    }
}
