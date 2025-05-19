package concurrency.server;

import concurrency.monitor.StatsAggregator;
import concurrency.queue.RequestQueue;
import core.documents.Document;
import core.memento.DocumentHistoryLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WorkerPool {

    private final ExecutorService exec;
    private final List<Worker> list = new ArrayList<>();

    public WorkerPool(int size, RequestQueue q, Document d,
                      DocumentHistoryLogger h, StatsAggregator a) {

        exec = Executors.newFixedThreadPool(size);
        for (int i = 1; i <= size; i++) {
            Worker w = new Worker(i, q, d, h, a);
            list.add(w);
            exec.submit(w);
        }
    }

    public void shutdownGracefully() {
        exec.shutdown();
        try {
            if (!exec.awaitTermination(10, TimeUnit.SECONDS))
                exec.shutdownNow();
        } catch (InterruptedException ie) {
            exec.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public int size() {
        return list.size();
    }
}