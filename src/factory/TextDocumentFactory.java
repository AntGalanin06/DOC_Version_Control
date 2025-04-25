package factory;

import documents.Document;
import documents.TextDocument;

public class TextDocumentFactory implements DocumentFactory {
    @Override
    public Document createDocument() {
        return new TextDocument();
    }
}