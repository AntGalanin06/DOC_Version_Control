package decorators;

import documents.Document;
import logger.LogCollector;
import memento.DocumentMemento;
import observer.DocumentObserver;

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
        LogCollector.getInstance().add("CREATE memento");
        return super.createMemento();
    }

    @Override
    public void restoreFromMemento(DocumentMemento memento) {
        LogCollector.getInstance().add("RESTORE from memento");
        super.restoreFromMemento(memento);
    }

    @Override
    public void addObserver(DocumentObserver observer) {
        LogCollector.getInstance().add("ADD observer");
        super.addObserver(observer);
    }

    @Override
    public void removeObserver(DocumentObserver observer) {
        LogCollector.getInstance().add("REMOVE observer");
        super.removeObserver(observer);
    }
}