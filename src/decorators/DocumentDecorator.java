package decorators;

import documents.Document;
import memento.DocumentMemento;

public abstract class DocumentDecorator extends Document {
    protected Document document;

    public DocumentDecorator(Document document) {
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
}
