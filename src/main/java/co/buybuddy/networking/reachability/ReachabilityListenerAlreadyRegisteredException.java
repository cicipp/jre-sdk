package co.buybuddy.networking.reachability;

public class ReachabilityListenerAlreadyRegisteredException extends Exception {
    private ReachabilityListener listener;

    public ReachabilityListenerAlreadyRegisteredException(ReachabilityListener listener) {
        super("Reachability listener already registered: " + listener.toString());

        this.listener = listener;
    }
}
