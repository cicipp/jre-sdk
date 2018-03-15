package com.buybuddy.networking.reachability;

public interface ReachabilityListener {
    void reachabilityDidChange(ReachabilityStatus status);
}
