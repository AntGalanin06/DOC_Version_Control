package core.logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogCollector {

    private static final LogCollector INSTANCE = new LogCollector();
    private static final int MAX_LOGS = 10_000;                // кольцевой буфер

    private final CopyOnWriteArrayList<String> logs = new CopyOnWriteArrayList<>();

    private LogCollector() {}

    public static LogCollector getInstance() { return INSTANCE; }

    public void add(String message) {
        if (logs.size() >= MAX_LOGS) {
            int trim = MAX_LOGS / 10;
            logs.subList(0, trim).clear();
        }
        logs.add(message);
    }

    public List<String> getLogs() { return List.copyOf(logs); }
}