package co.buybuddy.networking.reachability;

import java.util.ArrayList;

/**
 * An abstract class providing strict schematron for reachability managers.
 *
 * One of the concrete implementations is polling reachability manager, which uses
 * HTTP polling for understand the current reachability status of the device.
 */
public abstract class ReachabilityManager {
    protected ArrayList<ReachabilityListener> listeners;
    protected ReachabilityStatus currentStatus;

    public ReachabilityManager() {
        listeners = new ArrayList<>();
        currentStatus = ReachabilityStatus.UNKNOWN;
    }

    /**
     * Registers a listener object to be notified about changes in reachability status.
     * @param listener Listener object to be registered.
     * @throws ReachabilityListenerAlreadyRegisteredException Thrown when given listener object is already registered.
     */
    public void registerListener(ReachabilityListener listener) throws ReachabilityListenerAlreadyRegisteredException {
        if (listeners.contains(listener)) {
            throw new ReachabilityListenerAlreadyRegisteredException(listener);
        }

        listeners.add(listener);
    }

    /**
     * Returns registered listeners to the manager.
     * @return Currently registered listeners.
     */
    public ArrayList<ReachabilityListener> getListeners() {
        return (ArrayList)listeners.clone();
    }
}
