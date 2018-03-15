package com.buybuddy.networking.metrics;

import java.util.ArrayList;
import java.util.Queue;

public class IOMetricsRepository {
    private Queue<IOMetrics> metricsQueue;

    /**
     * Pushes metrics to the repository immediately.
     */
    public void pushImmediately() {
        ArrayList metricsToBeSent = new ArrayList(this.metricsQueue);

        //  TODO: Push metrics to the central repository.

        metricsQueue.clear();
    }
}
