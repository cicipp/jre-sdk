package co.buybuddy.networking.http;

import okhttp3.OkHttpClient;

public class HttpClientFactory {
    protected OkHttpClient client;

    public HttpClientFactory() {
        this.client = new OkHttpClient.Builder()
                .build();
    }

    public OkHttpClient getClient() {
        return client;
    }
}
