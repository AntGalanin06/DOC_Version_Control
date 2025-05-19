package core.factory;

import core.documents.Document;
import core.documents.PdfDocument;

public class PdfDocumentFactory implements DocumentFactory {
    @Override
    public Document createDocument() {
        return new PdfDocument();
    }
}