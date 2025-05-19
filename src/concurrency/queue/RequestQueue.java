package concurrency.queue;

import concurrency.model.Request;
import concurrency.monitor.StatsAggregator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RequestQueue {

    private final BlockingQueue<Request> queue;
    private final StatsAggregator agg;

    public RequestQueue(int capacity, StatsAggregator agg) {
        this.queue = new LinkedBlockingQueue<>(capacity);
        this.agg = agg;
    }

    public void put(Request r) throws InterruptedException {
        queue.put(r);
        recordEnqueue(r);
    }

    public boolean offer(Request r, long timeout, TimeUnit unit) throws InterruptedException {
        boolean ok = queue.offer(r, timeout, unit);
        if (ok) recordEnqueue(r);
        return ok;
    }

    public Request take() throws InterruptedException {
        Request r = queue.take();
        agg.dequeue(r);
        agg.setQueueSize(queue.size());
        return r;
    }

    public int size() { return queue.size(); }

    private void recordEnqueue(Request r) {
        agg.incProducedTotal();
        agg.enqueue(r);
        agg.setQueueSize(queue.size());
    }
}