package concurrency.client;

import concurrency.model.RequestType;

public record ClientConfig(
        int id,
        RequestType[] allowedTypes,
        long baseDelayMs,
        long jitterMs,
        int burst
) { }