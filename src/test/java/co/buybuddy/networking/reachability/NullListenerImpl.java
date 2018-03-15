package co.buybuddy.networking.reachability;

public class NullListenerImpl implements ReachabilityListener {
    @Override
    public void reachabilityDidChange(ReachabilityStatus status) {
        //  Do nothing.
    }
}
