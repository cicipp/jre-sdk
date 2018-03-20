package co.buybuddy.networking.authentication.tfa;

import co.buybuddy.networking.authentication.ContextResolutionError;
import co.buybuddy.networking.authentication.primitives.OneTimeCode;

/**
 * An interface to observe the authentication flow happening in context.
 */
public interface ContextProcessObserver {
    /**
     * Called when two-factor authentication over SMS is required. It should return a `OneTimeCode` instance
     * if client implementation is managed to gather the data from the user. HttpClientFactory-side implementation might pass
     * `null` if code was not entered by the user, in order to reset the authentication process.
     *
     * @return An `OneTimeCode` object constructed from the data taken from the user.
     * @throws ContextResolutionError This exception is thrown when this method is not implemented, but it is
     * called by the context manager.
     */
    public default OneTimeCode requiresSmsAuthentication() {
        throw new ContextResolutionError(ContextProcessType.ONE_TIME_CODE_OVER_SMS);
    }

    /**
     * Called when two-factor authentication over email is required. It should return a `OneTimeCode` instance
     * if client implementation is managed to gather the data from the user. HttpClientFactory-side implementation might pass
     * `null` if code was not entered by the user, in order to reset the authentication process.
     * @return An `OneTimeCode` object constructed from the data taken from the user.
     * @throws ContextResolutionError This exception is thrown when this method is not implemented, but it is
     * called by the context manager.
     */
    public default OneTimeCode requiresEmailAuthentication() {
        throw new ContextResolutionError(ContextProcessType.ONE_TIME_CODE_OVER_EMAIL);
    }

    /**
     * Called when two-factor authentication over an external authenticator is required. It should return
     * a `OneTimeCode` instance if client implementation is managed to gather the data from the user.
     * HttpClientFactory-side implementation might pass `null` if code was not entered by the user, in order to reset
     * the authentication process.
     * @return An `OneTimeCode` object constructed from the data taken from the user.
     * @throws ContextResolutionError This exception is thrown when this method is not implemented, but it is
     * called by the context manager.
     */
    public default OneTimeCode requiresExternalAuthentication() {
        throw new ContextResolutionError(ContextProcessType.ONE_TIME_CODE_OVER_EXTERNAL_AUTHENTICATOR);
    }

    /**
     * Called when authentication flow is successfully completed.
     */
    public void didOpen();
}
