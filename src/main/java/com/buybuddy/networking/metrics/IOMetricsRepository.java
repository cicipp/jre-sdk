package com.buybuddy.networking.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class IOMetricsRepository {
    private Queue<IOMetrics> metricsQueue;

    /**
     * Pushes metrics to the repository immediately.
     */
    public void pushImmediately() {
        List<IOMetrics> metricsToBeSent = new ArrayList(this.metricsQueue.toArray());

        //  TODO: Push metrics to the central repository.

        metricsQueue.clear();
    }
}
