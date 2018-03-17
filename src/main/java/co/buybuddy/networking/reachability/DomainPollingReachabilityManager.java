package co.buybuddy.networking.reachability;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang3.Validate;

public class DomainPollingReachabilityManager extends ReachabilityManager {
    private static int POLLING_START_DELAY = 0;
    private static AtomicLong COUNTER = new AtomicLong(0);
    private static String TIMER_NAME_PREFIX = "co.buybuddy.DomainPollingReachabilityManager.timer-";
    private static boolean USES_DAEMON_THREAD = true;

    protected String domain;
    private InetAddress address;
    protected int timeout;
    private String timerName;
    private Timer timer;
    protected int interval;
    private boolean awaitsListener;
    private Date lastPollTimestamp;

    public DomainPollingReachabilityManager(String domain, int timeout, int interval) throws UnknownHostException {
        super();

        Validate.notNull(domain, "domain should not be null");

        this.domain = domain;
        this.address = InetAddress.getByName(this.domain);
        this.timeout = timeout;
        this.interval = interval;
        this.awaitsListener = false;
    }

    public void startPolling() {
        if (!getListeners().isEmpty()) {
            //  We will start to polling immediately since there are listeners.
            timerName = TIMER_NAME_PREFIX + Long.toString(COUNTER.getAndIncrement());
            timer = new Timer(timerName, USES_DAEMON_THREAD);

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        DomainPollingReachabilityManager.this.poll();
                    } catch (IOException exception) {
                        DomainPollingReachabilityManager.this.currentStatus = ReachabilityStatus.UNKNOWN;
                    }
                }
            }, POLLING_START_DELAY, this.interval);
        } else {
            //  Let's wait for a listener.
            awaitsListener = true;
        }
    }

    public void stopPolling() {
        timer.cancel();
    }

    private void poll() throws IOException {
        boolean reachable = address.isReachable(this.timeout);

        if (reachable) {
            currentStatus = ReachabilityStatus.REACHABLE_WITH_ANYTHING;
        } else {
            currentStatus = ReachabilityStatus.NOT_REACHABLE;
        }

        lastPollTimestamp = new Date();
    }

    @Override
    public void registerListener(ReachabilityListener listener) throws ReachabilityListenerAlreadyRegisteredException {
        super.registerListener(listener);

        if (awaitsListener) {
            startPolling();
        }
    }

    public String getDomain() {
        return domain;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getTimerName() {
        return timerName;
    }

    public int getInterval() {
        return interval;
    }
}
