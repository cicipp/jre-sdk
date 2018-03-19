package co.buybuddy.networking.authentication;

import co.buybuddy.networking.authentication.tfa.ContextProcessType;

public class ContextResolutionException extends Exception {
    private ContextProcessType type;

    public ContextResolutionException(ContextProcessType type) {
        super("context cannot be resolved since no behavior implemented for type: " + type.toString());

        this.type = type;
    }

    public ContextProcessType getType() {
        return type;
    }
}
