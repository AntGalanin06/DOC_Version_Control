package logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogCollector {
    private static final LogCollector INSTANCE = new LogCollector();
    private final List<String> logs = new ArrayList<>();

    private LogCollector() { }

    public static LogCollector getInstance() {
        return INSTANCE;
    }

    public void add(String message) {
        logs.add(message);
    }

    public List<String> getLogs() {
        return Collections.unmodifiableList(logs);
    }
}