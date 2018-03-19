package co.buybuddy.networking.authentication.tfa;

/**
 * Represents the authentication flow required in context.
 */
public enum ContextProcessType {
    /**
     * Represents insecure authentication scheme.
     */
    INSECURE,

    /**
     * Represents two-factor authentication (2FA) working with SMS.
     */
    ONE_TIME_CODE_OVER_SMS,

    /**
     * Represents two-factor authentication (2FA) working with email.
     */
    ONE_TIME_CODE_OVER_EMAIL,

    /**
     * Represents two-factor authentication (2FA) working with external authenticator (i.e. Google Authenticator).
     */
    ONE_TIME_CODE_OVER_EXTERNAL_AUTHENTICATOR,
}
