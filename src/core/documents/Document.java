package core.documents;

import core.memento.DocumentHistoryLogger;
import core.memento.DocumentMemento;
import core.observer.DocumentObserver;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Document {
    protected DocumentHistoryLogger historyLogger;

    public void setHistoryLogger(DocumentHistoryLogger logger) {
        this.historyLogger = logger;
    }

    private final CopyOnWriteArrayList<DocumentObserver> observers = new CopyOnWriteArrayList<>();

    public abstract String getContent();
    public abstract void setContent(String content);

    public abstract DocumentMemento createMemento();
    public abstract void restoreFromMemento(DocumentMemento memento);

    public void addObserver(DocumentObserver observer) {
        if (observer != null) observers.addIfAbsent(observer);
    }

    public void removeObserver(DocumentObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {
        for (DocumentObserver obs : observers) obs.update(this);
    }
}