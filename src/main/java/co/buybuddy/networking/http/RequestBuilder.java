package co.buybuddy.networking.http;

import co.buybuddy.networking.http.pathgen.UrlBuilder;
import co.buybuddy.networking.wsdl.Operation;
import co.buybuddy.networking.wsdl.OperationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.inject.Inject;
import java.net.URL;
import java.util.Map;

public class RequestBuilder extends Request.Builder {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    @Inject
    private UrlBuilder urlBuilder;
    
    private String method;

    @Inject
    private OperationRepository repository;

    @Override
    public RequestBuilder url(HttpUrl url) {
        return (RequestBuilder)super.url(url);
    }

    @Override
    public RequestBuilder url(String url) {
        return (RequestBuilder)super.url(url);
    }

    @Override
    public RequestBuilder url(URL url) {
        return (RequestBuilder)super.url(url);
    }

    @Override
    public RequestBuilder header(String name, String value) {
        return (RequestBuilder)super.header(name, value);
    }

    @Override
    public RequestBuilder addHeader(String name, String value) {
        return (RequestBuilder)super.addHeader(name, value);
    }

    @Override
    public RequestBuilder removeHeader(String name) {
        return (RequestBuilder)super.removeHeader(name);
    }

    @Override
    public RequestBuilder headers(Headers headers) {
        return (RequestBuilder)super.headers(headers);
    }

    @Override
    public RequestBuilder cacheControl(CacheControl cacheControl) {
        return (RequestBuilder)super.cacheControl(cacheControl);
    }

    @Override
    public RequestBuilder get() {
        return (RequestBuilder)super.get();
    }

    @Override
    public RequestBuilder head() {
        return (RequestBuilder)super.head();
    }

    @Override
    public RequestBuilder post(RequestBody body) {
        return (RequestBuilder)super.post(body);
    }

    @Override
    public RequestBuilder delete(RequestBody body) {
        return (RequestBuilder)super.delete(body);
    }

    @Override
    public RequestBuilder delete() {
        return (RequestBuilder)super.delete();
    }

    @Override
    public RequestBuilder put(RequestBody body) {
        return (RequestBuilder)super.put(body);
    }

    @Override
    public RequestBuilder patch(RequestBody body) {
        return (RequestBuilder)super.patch(body);
    }

    @Override
    public RequestBuilder method(String method, RequestBody body) {
        return (RequestBuilder)super.method(method, body);
    }

    @Override
    public RequestBuilder tag(Object tag) {
        return (RequestBuilder)super.tag(tag);
    }

    /**
     * Sets the operation on new request object.
     * @param name Name of the operation.
     * @param params A map of parameters supplying URI arguments.
     * @return Returns the builder instance.
     */
    public RequestBuilder operation(String name, Map<String, String> params) {
        Operation operation = repository.findOperationByName(name);

        this.method = operation.getMethod();

        String uri = operation.getUri(params);

        return this
                .url(urlBuilder.absoluteUrlForPath(uri))
                .method(operation.getMethod(), null);
    }

    /**
     * Serializes given object as JSON data in the payload of the request.
     * @param object Object to be serialized.
     * @return Returns the builder instance.
     * @throws JsonProcessingException Thrown when error happened during serialization.
     */
    public RequestBuilder serialize(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return (RequestBuilder)this.method(this.method, RequestBody.create(MEDIA_TYPE_JSON, mapper.writeValueAsString(object)));
    }
}
