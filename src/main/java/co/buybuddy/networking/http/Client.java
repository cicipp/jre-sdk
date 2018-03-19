package co.buybuddy.networking.http;

import co.buybuddy.Context;
import okhttp3.*;

import java.io.IOException;

public class Client {
    private final OkHttpClient client;
    private Context context;

    public Client(Context context) {
        this.context = context;

        client = new OkHttpClient.Builder()
                .authenticator(new PassphraseAuthenticatorImpl(context))
                .build();
    }

    public OkHttpClient getClient() {
        return client;
    }

    public Context getContext() {
        return context;
    }
}
