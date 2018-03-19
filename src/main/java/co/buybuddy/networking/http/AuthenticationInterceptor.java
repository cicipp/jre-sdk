package co.buybuddy.networking.http;

import co.buybuddy.Context;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import java.io.IOException;

public class AuthenticationInterceptor implements Interceptor {
    private Context context;
    private final Object lock;

    public AuthenticationInterceptor(Context context) {
        this.context = context;
        this.lock = new Object();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer " + context.getArtifactStorage().toUriSafeEncodedString())
                .build();

        Response response = chain.proceed(request);

        if (response.code() == 401) {
            synchronized (lock) {
                //  TODO: Take JWT.
            }
        }

        return response;
    }
}
