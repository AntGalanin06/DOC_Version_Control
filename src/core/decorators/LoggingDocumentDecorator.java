package core.decorators;

import core.documents.Document;
import core.logger.LogCollector;
import core.memento.DocumentMemento;
import core.observer.DocumentObserver;

public class LoggingDocumentDecorator extends DocumentDecorator {
    public LoggingDocumentDecorator(Document document) {
        super(document);
    }

    @Override
    public String getContent() {
        LogCollector.getInstance().add("GET content");
        return super.getContent();
    }

    @Override
    public void setContent(String content) {
        LogCollector.getInstance().add("SET content");
        super.setContent(content);
    }

    @Override
    public DocumentMemento createMemento() {
        LogCollector.getInstance().add("CREATE core.memento");
        return super.createMemento();
    }

    @Override
    public void restoreFromMemento(DocumentMemento memento) {
        LogCollector.getInstance().add("RESTORE from core.memento");
        super.restoreFromMemento(memento);
    }

    @Override
    public void addObserver(DocumentObserver observer) {
        LogCollector.getInstance().add("ADD core.observer");
        super.addObserver(observer);
    }

    @Override
    public void removeObserver(DocumentObserver observer) {
        LogCollector.getInstance().add("REMOVE core.observer");
        super.removeObserver(observer);
    }
}