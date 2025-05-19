package concurrency.monitor;

import concurrency.model.Request;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class StatsAggregator {

    private final int capacity;
    private volatile int queueSize;

    private final ConcurrentMap<Integer, ClientStat> clients = new ConcurrentHashMap<>();
    private final ConcurrentMap<Integer, WorkerStat> workers = new ConcurrentHashMap<>();

    private final List<Request> allRequests = Collections.synchronizedList(new ArrayList<>());
    private final ConcurrentMap<Long, Request> queued = new ConcurrentHashMap<>();

    private final AtomicLong producedTotal = new AtomicLong();
    private final AtomicLong consumedTotal = new AtomicLong();

    public StatsAggregator(int capacity) {
        this.capacity = capacity;
    }

    public void setQueueSize(int size) {
        queueSize = size;
    }

    public int capacity() {
        return capacity;
    }

    public int queueSize() {
        return queueSize;
    }

    public void incProducedTotal() {
        producedTotal.incrementAndGet();
    }

    public void incConsumedTotal() {
        consumedTotal.incrementAndGet();
    }

    public long producedTotal() {
        return producedTotal.get();
    }

    public long consumedTotal() {
        return consumedTotal.get();
    }

    public ClientStat client(int id) {
        return clients.computeIfAbsent(id, k -> new ClientStat());
    }

    public WorkerStat worker(int id) {
        return workers.computeIfAbsent(id, k -> new WorkerStat());
    }

    public void enqueue(Request r) {
        allRequests.add(r);
        queued.put(r.getId(), r);
    }

    public void dequeue(Request r) {
        queued.remove(r.getId());
    }

    public List<Request> allRequests() {
        return List.copyOf(allRequests);
    }

    public List<Request> queuedRequests() {
        return List.copyOf(queued.values());
    }

    public List<Object[]> clientRows() {
        List<Object[]> rows = new ArrayList<>();
        clients.forEach((id, st) ->
                rows.add(new Object[]{id, st.produced(), String.format("%.0f", st.avgDelay())}));
        rows.sort(Comparator.comparingLong(o -> -(Long) o[1]));
        return rows;
    }

    public List<Object[]> workerRows() {
        List<Object[]> rows = new ArrayList<>();
        workers.forEach((id, st) ->
                rows.add(new Object[]{id, st.consumed(), st.isBusy() ? "Busy" : "Idle"}));
        rows.sort(Comparator.comparingLong(o -> -(Long) o[1]));
        return rows;
    }
}