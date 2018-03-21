package co.buybuddy.networking.authentication;

import co.buybuddy.networking.authentication.tfa.ContextProcessType;

public class ContextResolutionError extends NoSuchMethodError {
    private ContextProcessType type;

    public ContextResolutionError(ContextProcessType type) {
        super("context cannot be resolved since no behavior implemented for type: " + type.toString());

        this.type = type;
    }

    public ContextProcessType getType() {
        return type;
    }
}
