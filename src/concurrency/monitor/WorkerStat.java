package concurrency.monitor;

import java.util.concurrent.atomic.AtomicLong;

public final class WorkerStat {
    private final AtomicLong consumed = new AtomicLong();
    private final AtomicLong busyUntil = new AtomicLong();

    private static final long BUSY_WINDOW_MS = 150;

    public void markBusy() {
        busyUntil.set(System.currentTimeMillis() + BUSY_WINDOW_MS);
    }

    public void incConsumed() {
        consumed.incrementAndGet();
    }

    public long consumed() {
        return consumed.get();
    }

    public boolean isBusy() {
        return busyUntil.get() > System.currentTimeMillis();
    }
}