package ru.doc.logging;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class LogCollector {
    public enum Level { INFO, DEBUG, ERROR }
    public enum Category {
        DOCUMENT, HISTORY, FACTORY, DECORATOR, UI
    }
    public enum OperationType {
        CALL("→"), RETURN("←"), ERROR("⚠");
        private final String symbol;
        OperationType(String symbol) {
            this.symbol = symbol;
        }
        public String getSymbol() {
            return symbol;
        }
    }
    private final List<String> records = new CopyOnWriteArrayList<>();
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public void add(Level level, Category category, OperationType type, String msg) {
        String record = String.format("[%s] [%s] [%s] [%s] %s",
                LocalDateTime.now().format(timeFormatter),
                level, category, type.getSymbol(), msg);
        records.add(record);
    }

    public void add(Level level, String msg) {
        add(level, Category.DOCUMENT, OperationType.CALL, msg);
    }

    public void add(String msg) {
        add(Level.INFO, msg);
    }

    public List<String> getRecordsByCategory(Category category) {
        return records.stream()
                .filter(r -> r.contains("[" + category + "]"))
                .collect(Collectors.toList());
    }

    public List<String> getRecordsByCategoryAndLevel(Category category, Level level) {
        return records.stream()
                .filter(r -> r.contains("[" + category + "]") && r.contains("[" + level + "]"))
                .collect(Collectors.toList());
    }

    public List<String> getRecords() {
        return Collections.unmodifiableList(records);
    }

    public List<String> getRecordsByLevel(Level level) {
        return records.stream()
                .filter(r -> r.contains("[" + level + "]"))
                .collect(Collectors.toList());
    }

    public void clear() {
        records.clear();
    }
}