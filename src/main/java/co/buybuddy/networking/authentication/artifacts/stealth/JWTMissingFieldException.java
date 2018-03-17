package co.buybuddy.networking.authentication.artifacts.stealth;

public class JWTMissingFieldException extends Exception {
    private final String fieldName;

    public JWTMissingFieldException(String fieldName) {
        super("field is missing: " + fieldName);

        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
