package factory;

import documents.Document;
import documents.SpreadsheetDocument;

public class SpreadsheetDocumentFactory implements DocumentFactory {
    @Override
    public Document createDocument(String type) {
        return new SpreadsheetDocument();
    }
}