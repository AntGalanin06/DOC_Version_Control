package concurrency.client;

import concurrency.model.RequestType;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public final class ClientConfigFactory {
    private ClientConfigFactory() {}
    public static ClientConfig random(int id) {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        RequestType[] pool = {
                RequestType.CHANGE_TEXT,
                RequestType.UNDO,
                RequestType.REDO,
                RequestType.REVERT
        };
        int n = 1 + r.nextInt(pool.length);
        Set<RequestType> chosen = new LinkedHashSet<>();
        while (chosen.size() < n) {
            chosen.add(pool[r.nextInt(pool.length)]);
        }
        long baseDelay = 200 + r.nextLong(601);
        long jitter = 100 + r.nextLong(401);
        int burst = 1 + r.nextInt(4);
        return new ClientConfig(id, chosen.toArray(RequestType[]::new), baseDelay, jitter, burst);
    }
}