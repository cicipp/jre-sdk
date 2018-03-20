package co.buybuddy.networking.wsdl;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

public class Operation {
    private static final String URL_DELIMITER = "/";
    private static final String PARAM_START_DELIMITER = ":";
    private static final Pattern SLASH_PATTERN = Pattern.compile(Pattern.quote(URL_DELIMITER));

    public enum Action {
        LIST,
        SHOW,
        CREATE,
        UPDATE,
        DELETE,
    }

    private Action action;
    private String name;
    private String uriTemplate;

    public Operation(Action action, String name, String uriTemplate) {
        this.action = action;
        this.name = name;
        this.uriTemplate = uriTemplate;
    }

    public Action getAction() {
        return action;
    }

    public String getName() {
        return name;
    }

    public String getUriTemplate() {
        return uriTemplate;
    }

    public String getUri(Map<String, String> params) {
        String[] templatePieces = SLASH_PATTERN.split(uriTemplate);
        ArrayList<String> parameterizedPieces = new ArrayList<>();

        for (String piece : templatePieces) {
            if (piece.startsWith(PARAM_START_DELIMITER)) {
                String pieceTag = piece.substring(1);

                parameterizedPieces.add(params.get(pieceTag));
            } else {
                parameterizedPieces.add(piece);
            }
        }

        return String.join(URL_DELIMITER, parameterizedPieces);
    }

    public String getMethod() {
        switch (action) {
            case LIST:
            case SHOW:
                return "GET";
            case CREATE:
                return "POST";
            case UPDATE:
                return "PUT";
            case DELETE:
                return "DELETE";
        }

        throw new InternalError("unsupported method type");
    }
}
