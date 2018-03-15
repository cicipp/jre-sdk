package co.buybuddy.networking.authentication.primitives;

public class Credentials {
    /**
     * An identifier of a platform user, consisting email or username.
     */
    private String identifier;

    /**
     * Password of the platform user with given identifier.
     */
    private String password;

    public Credentials(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getPassword() {
        return password;
    }
}
