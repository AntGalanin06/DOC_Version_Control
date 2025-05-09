package ru.doc.decorators;

import ru.doc.document.Document;
import ru.doc.memento.DocumentMemento;
import ru.doc.observer.DocumentObserver;

public abstract class DocumentDecorator extends Document {
    protected Document document;

    public DocumentDecorator(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("Wrapped document cannot be null");
        }
        this.document = document;
    }

    @Override
    public String getContent() {
        return document.getContent();
    }

    @Override
    public void setContent(String content) {
        document.setContent(content);
    }

    @Override
    public DocumentMemento createMemento() {
        return document.createMemento();
    }

    @Override
    public void restoreFromMemento(DocumentMemento memento) {
        document.restoreFromMemento(memento);
    }

    @Override
    public void addObserver(DocumentObserver observer) {
        document.addObserver(observer);
    }

    @Override
    public void removeObserver(DocumentObserver observer) {
        document.removeObserver(observer);
    }
}