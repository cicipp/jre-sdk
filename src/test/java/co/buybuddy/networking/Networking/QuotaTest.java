package co.buybuddy.networking.Networking;

import co.buybuddy.networking.RateLimiting.Quota;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class QuotaTest {

    public static final Date resetDate = new Date();

    @Test
    public void constructsQuota() {
        Quota quota = new Quota(80, 100, resetDate);

        assertNotNull(quota);
        assertEquals(quota.getMaximumAmount(), 100);
        assertEquals(quota.getConsumedAmount(), 80);
        assertEquals(quota.getQuotaResetDate(), resetDate);
    }

    @Test
    public void calculatesRemainingAmountWithAvailability() {
        Quota quota = new Quota(80, 100, resetDate);

        assertEquals(quota.getRemainingAmount(),quota.getMaximumAmount()-quota.getConsumedAmount());
        assertTrue(quota.isAvailable());
    }

    @Test
    public void calculatesRemainingAmountWithoutAvailability() {
        Quota quota = new Quota(100, 100, resetDate);

        assertEquals(quota.getRemainingAmount(), quota.getMaximumAmount()-quota.getConsumedAmount());
        assertFalse(quota.isAvailable());
    }
}
