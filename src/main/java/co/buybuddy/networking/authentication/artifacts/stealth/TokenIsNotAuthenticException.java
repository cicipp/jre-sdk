package co.buybuddy.networking.authentication.artifacts.stealth;

public class TokenIsNotAuthenticException extends Exception {
    public TokenIsNotAuthenticException() {
        super("given token is not issued by platform");
    }
}
