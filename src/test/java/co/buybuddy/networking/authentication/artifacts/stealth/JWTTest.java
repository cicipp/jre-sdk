package co.buybuddy.networking.authentication.artifacts.stealth;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class JWTTest {
    public static final String basePath = "opt/java/test/co/buybuddy/networking/authentication/artifacts/stealth";
    public static String validToken;
    public final String tokenWithoutIssuer;
    public final String tokenWithoutExpiration;
    public final String tokenWithInvalidIssuer;

    private static String readTokenToString(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(FileSystems.getDefault().getPath(basePath + path));

        return new String(bytes, Charset.forName("UTF-8"));
    }

    public JWTTest() throws IOException {
        validToken = readTokenToString( "/valid_token.txt");
        tokenWithoutIssuer = readTokenToString( "/token_without_iss.txt");
        tokenWithoutExpiration = readTokenToString( "/token_without_exp.txt");
        tokenWithInvalidIssuer = readTokenToString("/token_with_invalid_iss.txt");
    }

    @Test
    public void constructsTokenWithValidAttributes() throws JWTIsNotAuthenticException, IOException, JWTMissingFieldException {
        JWT jwt = new JWT(validToken);

        assertNotNull(jwt);
    }

    @Test
    public void throwsIfTokenDoesNotHaveIssField() throws IOException, JWTIsNotAuthenticException {
        try {
            new JWT(tokenWithoutIssuer);
        } catch (JWTMissingFieldException e) {
            assertEquals(e.getFieldName(), "iss");

            return;
        }

        assertTrue(false);
    }

    @Test()
    public void throwsIfTokenDoesNotHaveExpField() throws JWTIsNotAuthenticException, IOException {
        try {
            new JWT(tokenWithoutExpiration);
        } catch (JWTMissingFieldException e) {
            assertEquals(e.getFieldName(), "exp");

            return;
        }

        assertTrue(false);
    }

    @Test(expected = JWTIsNotAuthenticException.class)
    public void throwsIfIssIsNotAuthentic() throws JWTIsNotAuthenticException, IOException, JWTMissingFieldException {
        new JWT(tokenWithInvalidIssuer);
    }
}
