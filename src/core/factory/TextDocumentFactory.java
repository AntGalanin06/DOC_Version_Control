package core.factory;

import core.documents.Document;
import core.documents.TextDocument;

public class TextDocumentFactory implements DocumentFactory {
    @Override
    public Document createDocument() {
        return new TextDocument();
    }
}