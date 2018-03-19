package co.buybuddy.networking.authentication.artifacts.stealth;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Token {
    private static final String EXPIRATION_FIELD = "exp";
    private static final String ISSUER_FIELD = "iss";
    private static final String TIMESTAMP_FIELD = "iat";
    private static final String SUBSIDIARY_FIELD = "sub";
    private static final String AUTHENTIC_ISSUER = "BuyBuddy";
    private static final Pattern JWT_SPLIT_REGEX = Pattern.compile(Pattern.quote("."));

    private String tokenString;
    private HashMap payload;
    private String issuer;
    private String subsidiary;
    private Date expiration;
    private Date issuedAt;

    private Object setPayloadField(String key) throws TokenMissingFieldException {
        if (!payload.containsKey(key)) {
            throw new TokenMissingFieldException(key);
        }

        return payload.get(key);
    }

    /**
     * Creates a new JSON Web Token (RFC 7519) with given tokenString string.
     *
     * This method should be used by the library, implicitly.
     * Users should not call this method.
     * @param token The tokenString string to represent Token.
     */
    public Token(String token) throws IOException, TokenMissingFieldException, TokenIsNotAuthenticException {
        this.tokenString = token;

        String payload = JWT_SPLIT_REGEX.split(this.tokenString)[1];

        if (payload != null) {
            byte[] payloadBytes = Base64.getDecoder().decode(payload);

            ObjectMapper mapper = new ObjectMapper();

            this.payload = mapper.readValue(payloadBytes, HashMap.class);

            expiration = new Date(Integer.toUnsignedLong((int)setPayloadField(EXPIRATION_FIELD)));
            issuer = (String)setPayloadField(ISSUER_FIELD);
            subsidiary = (String)setPayloadField(SUBSIDIARY_FIELD);
            issuedAt = new Date(Integer.toUnsignedLong((int)setPayloadField(TIMESTAMP_FIELD)));

            if (!issuer.equals(AUTHENTIC_ISSUER)) {
                throw new TokenIsNotAuthenticException();
            }
        }
    }

    /**
     * Indicates if tokenString is expired.
     * @return A boolean value indicating expiration status of the tokenString.
     */
    public boolean isExpired() {
        return expiration.before(new Date());
    }

    /**
     * Returns the data the tokenString is valid until.
     * @return The date.
     */
    public Date getExpiration() {
        return expiration;
    }

    /**
     * Returns the date the tokenString was issued by the central authority.
     * @return The date.
     */
    public Date getIssuedAt() {
        return issuedAt;
    }

    /**
     * Token body of the Token.
     * @return Body of the Token.
     */
    @Override
    public String toString() {
        return tokenString;
    }

    /**
     * Payload of the Token.
     * @return An hash map containing key-value pairs found in payload.
     */
    public HashMap getPayload() {
        return payload;
    }

    public String getSubsidiary() {
        return subsidiary;
    }
}
