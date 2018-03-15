package com.buybuddy.networking.reachability;

/**
 * Shows reachability status.
 *
 * If reachability is `UNKNOWN`, the status cannot be determined.
 * If it is `NOT_REACHABLE`, the device has no reachability.
 * If there is a reachability, one of the `REACHABLE_WITH_WWAN`, `REACHABLE_WITH_WLAN` or `REACHABLE_WITH_ANYTHING`
 * should be used.
 *
 * `REACHABLE_WITH_WWAN` shows the device has reachability via cellular data.
 * `REACHABLE_WITH_WLAN` shows the device has reachability via wireless LAN.
 */
public enum ReachabilityStatus {
    UNKNOWN,
    NOT_REACHABLE,
    REACHABLE_WITH_WWAN,
    REACHABLE_WITH_WLAN,
    REACHABLE_WITH_ANYTHING,
}
