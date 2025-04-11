package proxy;

import documents.Document;
import factory.DocumentFactory;
import memento.DocumentMemento;

public class DocumentProxy extends Document {
    private Document realDocument;
    private DocumentFactory factory;
    private String type;

    public DocumentProxy(DocumentFactory factory, String type) {
        this.factory = factory;
        this.type = type;
    }

    private void loadDocument() {
        if (realDocument == null) {
            realDocument = factory.createDocument(type);
        }
    }

    @Override
    public String getContent() {
        loadDocument();
        return realDocument.getContent();
    }

    @Override
    public void setContent(String content) {
        loadDocument();
        realDocument.setContent(content);
    }

    @Override
    public DocumentMemento createMemento() {
        loadDocument();
        return realDocument.createMemento();
    }

    @Override
    public void restoreFromMemento(DocumentMemento memento) {
        loadDocument();
        realDocument.restoreFromMemento(memento);
    }
}
