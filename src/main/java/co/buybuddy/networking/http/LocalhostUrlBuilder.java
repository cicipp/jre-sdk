package co.buybuddy.networking.http;

public class LocalhostUrlBuilder extends UrlBuilder {
    public static final String LOCALHOST_URL = "http://localhost";

    @Override
    public String absoluteUrlForPath(String path, int port) {
        return LOCALHOST_URL + ":" + port + path;
    }
}
