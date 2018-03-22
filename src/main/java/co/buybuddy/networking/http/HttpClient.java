package co.buybuddy.networking.http;

import co.buybuddy.networking.errorhandling.InternalInconsistencyException;
import co.buybuddy.networking.errorhandling.NetworkIOException;
import dagger.Module;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@Module
public class HttpClient extends OkHttpClient {
    public <ReturnType> ReturnType executeCall(Request request, HttpClientCallDelegate<ReturnType> delegate) throws NetworkIOException {
        Response response = null;
        try {
            response = this.newCall(request).execute();
        } catch (IOException ioe) {
            //  TODO: Report the error to the error reporter.

            throw new NetworkIOException("network error during platform call", ioe);
        }

        try {
            switch (response.code()) {
                case 100:
                    return delegate.onContinue(response);

                case 200:
                    return delegate.onOk(response);

                case 201:
                    return delegate.onCreated(response);

                case 204:
                    return delegate.onNoContent(response);

                case 400:
                    return delegate.onBadRequest(response);

                case 404:
                    return delegate.onNotFound(response);

                case 409:
                    return delegate.onConflict(response);

                case 422:
                    return delegate.onUnprocessableEntity(response);

                default:
                    throw new InternalInconsistencyException("request facade not implemented for " + request);
            }
        } catch (IOException ioe) {
            //  TODO: Report the error to the error reporter.

            throw new InternalInconsistencyException("error during response resolution", ioe);
        }
    }
}
