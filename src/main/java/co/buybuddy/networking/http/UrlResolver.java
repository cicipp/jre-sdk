package co.buybuddy.networking.http;

import java.util.Map;

public class UrlResolver {
    private Map<String, String> urlTable;

    public UrlResolver(Map<String, String> urlTable) {
        this.urlTable = urlTable;
    }

    public String urlForOperationId(String operationId) {
        return urlTable.get(operationId);
    }
}
