package concurrency.client;

import concurrency.model.*;
import concurrency.queue.RequestQueue;
import concurrency.monitor.StatsAggregator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientGenerator implements Runnable {

    private static final AtomicInteger TXT = new AtomicInteger();

    private final int id;
    private final ClientConfig cfg;
    private final RequestQueue q;
    private final StatsAggregator agg;
    private int remaining;
    private volatile boolean run = true;

    private long lastSent = System.currentTimeMillis();

    public ClientGenerator(int id, ClientConfig cfg, int quota,
                           RequestQueue q, StatsAggregator agg) {
        this.id = id;
        this.cfg = cfg;
        this.remaining = quota;
        this.q = q;
        this.agg = agg;
    }

    public void stop() {
        run = false;
    }

    @Override
    public void run() {
        var rnd = ThreadLocalRandom.current();
        var stat = agg.client(id);

        while (run && remaining > 0) {
            try {
                for (int i = 0; i < cfg.burst() && run && remaining > 0; i++) {
                    RequestType t = pick(rnd);
                    String c = t == RequestType.CHANGE_TEXT ? "txt-" + id + "-" + TXT.incrementAndGet() : null;
                    q.put(new Request(t, c));
                    remaining--;
                    long now = System.currentTimeMillis();
                    stat.addDelay(now - lastSent);
                    stat.incProduced();
                    lastSent = now;
                }
                long d = cfg.baseDelayMs() + rnd.nextLong(cfg.jitterMs());
                Thread.sleep(d);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private RequestType pick(ThreadLocalRandom r) {
        var arr = cfg.allowedTypes();
        return arr[r.nextInt(arr.length)];
    }
}