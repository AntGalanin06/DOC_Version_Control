package concurrency.monitor;

import java.util.concurrent.atomic.AtomicLong;

public final class ClientStat {
    private final AtomicLong produced = new AtomicLong();
    private final AtomicLong totalDelay = new AtomicLong();
    private final AtomicLong requests   = new AtomicLong();

    public void incProduced()                         { produced.incrementAndGet(); }
    public void addDelay(long d)                      { totalDelay.addAndGet(d); requests.incrementAndGet(); }
    public long produced()                            { return produced.get(); }
    public double avgDelay()                          { long r = requests.get(); return r == 0 ? 0 : (double) totalDelay.get() / r; }
}