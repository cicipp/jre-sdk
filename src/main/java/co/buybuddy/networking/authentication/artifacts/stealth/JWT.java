package co.buybuddy.networking.authentication.artifacts.stealth;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

public class JWT {
    private static final String EXPIRATION_FIELD = "exp";
    private static final String ISSUER_FIELD = "iss";
    private static final String TIMESTAMP_FIELD = "iat";
    private static final String SUBSIDIARY_FIELD = "sub";
    private static final String AUTHENTIC_ISSUER = "BuyBuddy";
    private static final Pattern JWT_SPLIT_REGEX = Pattern.compile(Pattern.quote("."));

    private String token;
    private HashMap payload;
    private String issuer;
    private String subsidiary;
    private Date expiration;
    private Date issuedAt;

    private Object setPayloadField(String key) throws JWTMissingFieldException {
        if (!payload.containsKey(key)) {
            throw new JWTMissingFieldException(key);
        }

        return payload.get(key);
    }

    /**
     * Creates a new JSON Web Token (RFC 7519) with given token string.
     *
     * This method should be used by the library, implicitly.
     * Users should not call this method.
     * @param token The token string to represent JWT.
     */
    public JWT(String token) throws IOException, JWTMissingFieldException, JWTIsNotAuthenticException {
        this.token = token;

        String payload = JWT_SPLIT_REGEX.split(this.token)[1];

        if (payload != null) {
            byte[] payloadBytes = Base64.getDecoder().decode(payload);

            ObjectMapper mapper = new ObjectMapper();

            this.payload = mapper.readValue(payloadBytes, HashMap.class);

            expiration = new Date(Integer.toUnsignedLong((int)setPayloadField(EXPIRATION_FIELD)));
            issuer = (String)setPayloadField(ISSUER_FIELD);
            subsidiary = (String)setPayloadField(SUBSIDIARY_FIELD);
            issuedAt = new Date(Integer.toUnsignedLong((int)setPayloadField(TIMESTAMP_FIELD)));

            if (!issuer.equals(AUTHENTIC_ISSUER)) {
                throw new JWTIsNotAuthenticException();
            }
        }
    }

    /**
     * Indicates if token is expired.
     * @return A boolean value indicating expiration status of the token.
     */
    public boolean isExpired() {
        return expiration.before(new Date());
    }

    /**
     * Returns the data the token is valid until.
     * @return The date.
     */
    public Date getExpiration() {
        return expiration;
    }

    /**
     * Returns the date the token was issued by the central authority.
     * @return The date.
     */
    public Date getIssuedAt() {
        return issuedAt;
    }

    /**
     * Token body of the JWT.
     * @return Body of the JWT.
     */
    public String getToken() {
        return token;
    }

    /**
     * Payload of the JWT.
     * @return An hash map containing key-value pairs found in payload.
     */
    public HashMap getPayload() {
        return payload;
    }

    public String getSubsidiary() {
        return subsidiary;
    }
}
