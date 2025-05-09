package ru.doc.document;

import ru.doc.history.DocumentHistoryLogger;
import ru.doc.memento.DocumentMemento;
import ru.doc.memento.GenericDocumentMemento;
import ru.doc.observer.DocumentObserver;
import ru.doc.logging.Loggable;
import ru.doc.logging.LogCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Document {
    private String content = "";
    private DocumentHistoryLogger historyLogger;
    private final List<DocumentObserver> observers = new ArrayList<>();

    @Loggable(category = LogCollector.Category.DOCUMENT, withResult = true)
    public String getContent() {
        return content;
    }

    @Loggable(category = LogCollector.Category.DOCUMENT, withArgs = true)
    public void setContent(String content) {
        this.content = content;
        notifyObservers();
    }

    public void setHistoryLogger(DocumentHistoryLogger historyLogger) {
        this.historyLogger = historyLogger;
    }

    @Loggable(category = LogCollector.Category.DOCUMENT, level = LogCollector.Level.DEBUG)
    public DocumentMemento createMemento() {
        Map<String, Object> state = new HashMap<>();
        state.put("content", content);
        return new GenericDocumentMemento(state);
    }

    @Loggable(category = LogCollector.Category.DOCUMENT, level = LogCollector.Level.DEBUG)
    public void restoreFromMemento(DocumentMemento memento) {
        Map<String, Object> state = memento.getState();
        this.content = (String) state.get("content");
        notifyObservers();
    }

    @Loggable(category = LogCollector.Category.DOCUMENT, level = LogCollector.Level.DEBUG)
    public void addObserver(DocumentObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(DocumentObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (DocumentObserver observer : observers) {
            observer.update(this);
        }
    }
}