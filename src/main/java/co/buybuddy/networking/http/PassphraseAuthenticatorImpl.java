package co.buybuddy.networking.http;

import co.buybuddy.Context;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import java.io.IOException;

public class PassphraseAuthenticatorImpl implements Authenticator {
    private Context context;

    public PassphraseAuthenticatorImpl(Context context) {
        this.context = context;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (new AuthorizationHeaderParser().parse(response) != null) {
            return null;
        }

        return response.request().newBuilder()
                .header("Authorization", "Bearer " + context.getArtifactStorage().toUriSafeEncodedString())
                .build();
    }
}
