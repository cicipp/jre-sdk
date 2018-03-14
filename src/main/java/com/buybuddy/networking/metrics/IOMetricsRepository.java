package com.buybuddy.networking.metrics;

import java.lang.reflect.Array;
import java.util.Queue;

public class IOMetricsRepository {
    private Queue<IOMetrics> metricsQueue;

    /**
     * Pushes metrics to the repository immediately.
     */
    public void pushImmediately() {
        Array<IOMetrics> metricsToBeSent = metricsQueue.toArray();

        //  TODO: Push metrics to the central repository.

        metricsQueue.clear();
    }
}
