package core.factory;

import core.documents.Document;
import core.documents.SpreadsheetDocument;

public class SpreadsheetDocumentFactory implements DocumentFactory {
    @Override
    public Document createDocument() {
        return new SpreadsheetDocument();
    }
}