package co.buybuddy.networking.authentication.artifacts.concrete;

import co.buybuddy.networking.authentication.primitives.Credentials;
import co.buybuddy.networking.http.HttpClientFactory;
import co.buybuddy.networking.http.RequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AuthenticationManager {
    private class SignInForm {
        private Credentials credentials;

        public SignInForm(Credentials credentials) {
            this.credentials = credentials;
        }
    }

    void signIn(Credentials credentials) {
        OkHttpClient client = new HttpClientFactory().getClient();

        SignInForm form = new SignInForm(credentials);

        try {
            Request request = new RequestBuilder()
                    .url("https://co.buybuddy/api/users/sign_in")
                    .payloadObject(form)
                    .build();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
