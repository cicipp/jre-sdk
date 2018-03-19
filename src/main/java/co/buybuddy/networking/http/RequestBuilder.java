package co.buybuddy.networking.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;

import java.net.URL;

public class RequestBuilder extends Request.Builder {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

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

    public RequestBuilder operation() {
        return this;
    }

    public RequestBuilder payloadObject(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return (RequestBuilder)this.post(RequestBody.create(MEDIA_TYPE_JSON, mapper.writeValueAsString(object)));
    }
}
