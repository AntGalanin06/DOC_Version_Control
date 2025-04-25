package factory;

import documents.Document;
import documents.PdfDocument;

public class PdfDocumentFactory implements DocumentFactory {
    @Override
    public Document createDocument() {
        return new PdfDocument();
    }
}