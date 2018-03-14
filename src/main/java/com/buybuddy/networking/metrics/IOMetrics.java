package com.buybuddy.networking.metrics;

import lombok.AllArgsConstructor;
import lombok.Getter;

class IOMetrics {
    @Getter
    private long DNSLookupTime;

    @Getter
    private long TCPBootstrappingTime;

    @Getter
    private long TLSHandshakingTime;

    @Getter
    private long serverProcessingTime;

    @Getter
    private long requestContentTransferTime;

    @Getter
    private long responseContentTransferTime;

    /**
     * Calculates round-trip time (RTT) of the HTTP request.
     * @return RTT value.
     */
    public long roundTripTime() {
        return this.getDNSLookupTime() +
                this.getTCPBootstrappingTime() +
                this.getRequestContentTransferTime() +
                this.getServerProcessingTime() +
                this.getResponseContentTransferTime();
    }
}
