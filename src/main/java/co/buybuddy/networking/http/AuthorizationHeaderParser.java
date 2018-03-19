package co.buybuddy.networking.http;

import co.buybuddy.networking.authentication.artifacts.concrete.Passphrase;
import okhttp3.Response;

import java.util.regex.Pattern;

public class AuthorizationHeaderParser {
    private static final Pattern PARSER_REGEX = Pattern.compile(Pattern.quote("Bearer "));

    String parse(Response response) {
        String headerValue = response.request().header("Authorization");

        String[] parts = PARSER_REGEX.split(headerValue);

        if (parts.length == 2) {
            return parts[1];
        } else {
            return null;
        }
    }
}
