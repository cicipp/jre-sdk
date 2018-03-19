package co.buybuddy.networking.authentication.artifacts.stealth;

public class TokenMissingFieldException extends Exception {
    private final String fieldName;

    public TokenMissingFieldException(String fieldName) {
        super("field is missing: " + fieldName);

        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
