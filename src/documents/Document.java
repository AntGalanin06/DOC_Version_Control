package documents;

import memento.DocumentHistoryLogger;
import memento.DocumentMemento;
import observer.DocumentObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class Document {
    protected DocumentHistoryLogger historyLogger;
    public void setHistoryLogger(DocumentHistoryLogger logger) {
        this.historyLogger = logger;
    }

    private final List<DocumentObserver> observers = new ArrayList<>();

    public abstract String getContent();
    public abstract void setContent(String content);

    public abstract DocumentMemento createMemento();
    public abstract void restoreFromMemento(DocumentMemento memento);

    public void addObserver(DocumentObserver observer) {
        if (observer != null && !observers.contains(observer)) observers.add(observer);
    }
    public void removeObserver(DocumentObserver observer) { observers.remove(observer); }

    protected void notifyObservers() {
        for (DocumentObserver obs : new ArrayList<>(observers)) obs.update(this);
    }
}