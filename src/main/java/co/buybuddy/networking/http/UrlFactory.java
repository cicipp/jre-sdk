package co.buybuddy.networking.http;

public class UrlFactory {
    private UrlResolver resolver;
    private UrlBuilder builder;

    public UrlFactory(UrlResolver resolver, UrlBuilder builder) {
        this.resolver = resolver;
        this.builder = builder;
    }
}
