package concurrency.engine;

import concurrency.client.ClientConfig;
import concurrency.client.ClientConfigFactory;
import concurrency.client.ClientGenerator;
import concurrency.monitor.StatsAggregator;
import concurrency.model.Request;
import concurrency.model.RequestType;
import concurrency.queue.RequestQueue;
import concurrency.server.WorkerPool;
import core.documents.Document;
import core.memento.DocumentHistoryLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ConcurrencyEngine {

    private final StatsAggregator agg;
    private final RequestQueue queue;
    private final WorkerPool workers;
    private final List<ClientGenerator> gens = new ArrayList<>();
    private final List<Thread> genThreads = new ArrayList<>();

    public ConcurrencyEngine(int clients, int wcnt, int cap, int total,
                             Document doc, DocumentHistoryLogger hist) {

        agg = new StatsAggregator(cap);
        queue = new RequestQueue(cap, agg);

        int quota = total / clients;

        for (int i = 1; i <= clients; i++) {
            ClientConfig cfg = ClientConfigFactory.random(i);
            ClientGenerator g = new ClientGenerator(i, cfg, quota, queue, agg);
            gens.add(g);
            Thread t = new Thread(g, "Client-" + i);
            genThreads.add(t);
            t.start();
        }

        workers = new WorkerPool(wcnt, queue, doc, hist, agg);
    }

    public StatsAggregator stats() {
        return agg;
    }

    public void shutdown() {
        int w = workers.size();
        for (int i = 0; i < w; i++) {
            try {
                queue.offer(new Request(RequestType.SHUTDOWN, null), 2, TimeUnit.SECONDS);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        gens.forEach(ClientGenerator::stop);
        genThreads.forEach(Thread::interrupt);
        for (Thread t : genThreads) {
            try {
                t.join(2000);
            } catch (InterruptedException ignored) {}
        }
        workers.shutdownGracefully();
    }
}