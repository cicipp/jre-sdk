package co.buybuddy.networking.reachability;

public interface ReachabilityListener {
    void reachabilityDidChange(ReachabilityStatus status);
}
