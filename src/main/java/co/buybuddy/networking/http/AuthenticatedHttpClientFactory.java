package co.buybuddy.networking.http;

import co.buybuddy.Context;

public class AuthenticatedHttpClientFactory extends HttpClientFactory {
    private Context context;

    public AuthenticatedHttpClientFactory(Context context) {
        super();

        this.context = context;
        this.client = (HttpClient)this.client.newBuilder()
                .addInterceptor(new AuthenticationInterceptor(context))
                .build();
    }

    public Context getContext() {
        return context;
    }
}
