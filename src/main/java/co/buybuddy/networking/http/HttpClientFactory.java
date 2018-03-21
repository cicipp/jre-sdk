package co.buybuddy.networking.http;

import okhttp3.OkHttpClient;

public class HttpClientFactory {
    protected HttpClient client;

    public HttpClientFactory() {
        this.client = (HttpClient)(new OkHttpClient.Builder().build());
    }

    public HttpClient getClient() {
        return client;
    }
}
