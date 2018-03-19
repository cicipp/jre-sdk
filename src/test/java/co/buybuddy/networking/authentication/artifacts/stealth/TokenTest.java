package co.buybuddy.networking.authentication.artifacts.stealth;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class TokenTest {
    public static final String basePath = "opt/java/test/co/buybuddy/networking/authentication/artifacts/stealth";
    public static String validToken;
    public final String tokenWithoutIssuer;
    public final String tokenWithoutExpiration;
    public final String tokenWithInvalidIssuer;

    private static String readTokenToString(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(FileSystems.getDefault().getPath(basePath + path));

        return new String(bytes, Charset.forName("UTF-8"));
    }

    public TokenTest() throws IOException {
        validToken = readTokenToString( "/valid_token.txt");
        tokenWithoutIssuer = readTokenToString( "/token_without_iss.txt");
        tokenWithoutExpiration = readTokenToString( "/token_without_exp.txt");
        tokenWithInvalidIssuer = readTokenToString("/token_with_invalid_iss.txt");
    }

    @Test
    public void constructsTokenWithValidAttributes() throws TokenIsNotAuthenticException, IOException, TokenMissingFieldException {
        Token token = new Token(validToken);

        assertNotNull(token);
    }

    @Test
    public void throwsIfTokenDoesNotHaveIssField() throws IOException, TokenIsNotAuthenticException {
        try {
            new Token(tokenWithoutIssuer);
        } catch (TokenMissingFieldException e) {
            assertEquals(e.getFieldName(), "iss");

            return;
        }

        assertTrue(false);
    }

    @Test()
    public void throwsIfTokenDoesNotHaveExpField() throws TokenIsNotAuthenticException, IOException {
        try {
            new Token(tokenWithoutExpiration);
        } catch (TokenMissingFieldException e) {
            assertEquals(e.getFieldName(), "exp");

            return;
        }

        assertTrue(false);
    }

    @Test(expected = TokenIsNotAuthenticException.class)
    public void throwsIfIssIsNotAuthentic() throws TokenIsNotAuthenticException, IOException, TokenMissingFieldException {
        new Token(tokenWithInvalidIssuer);
    }
}
