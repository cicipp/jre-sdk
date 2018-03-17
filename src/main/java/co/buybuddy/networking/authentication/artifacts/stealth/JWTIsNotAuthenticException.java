package co.buybuddy.networking.authentication.artifacts.stealth;

public class JWTIsNotAuthenticException extends Exception {
    public JWTIsNotAuthenticException() {
        super("given token is not issued by platform");
    }
}
