package co.buybuddy.networking.reachability;

import org.junit.Test;

import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.*;

public class DomainPollingReachabilityManagerTest {
    public static final String DOMAIN_NAME = "buybuddy.co";
    public static final int TIMEOUT = 5000;
    public static final int INTERVAL = 5000;

    @Test
    public void constructsManagerWithDomain() throws UnknownHostException {
        DomainPollingReachabilityManager manager =
                new DomainPollingReachabilityManager(DOMAIN_NAME, TIMEOUT, INTERVAL);

        assertNotNull(manager);
        assertEquals(manager.getDomain(), DOMAIN_NAME);
        assertEquals(manager.getTimeout(), TIMEOUT);
        assertEquals(manager.getInterval(), INTERVAL);
    }

    @Test(expected = NullPointerException.class)
    public void throwsExceptionWithNullDomain() throws UnknownHostException {
        new DomainPollingReachabilityManager(null, TIMEOUT, INTERVAL);
    }
}