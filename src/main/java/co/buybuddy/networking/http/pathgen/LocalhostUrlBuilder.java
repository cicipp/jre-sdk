package co.buybuddy.networking.http.pathgen;

import co.buybuddy.networking.http.pathgen.UrlBuilder;

public class LocalhostUrlBuilder extends UrlBuilder {
    public static final String LOCALHOST_URL = "http://localhost";
    private int port;

    @Override
    public String absoluteUrlForPath(String path) {
        return LOCALHOST_URL + ":" + port + path;
    }

    public LocalhostUrlBuilder(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
