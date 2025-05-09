package ru.doc.factory;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.doc.document.Document;
import ru.doc.document.PdfDocument;
import ru.doc.history.DocumentHistoryLogger;
import ru.doc.logging.Loggable;
import ru.doc.logging.LogCollector;

@Component
public class PdfDocumentFactory implements DocumentFactory {
    private final ObjectProvider<PdfDocument> pdfProvider;
    private final ObjectProvider<DocumentHistoryLogger> historyProvider;

    @Autowired
    public PdfDocumentFactory(ObjectProvider<PdfDocument> pdfProvider,
                              ObjectProvider<DocumentHistoryLogger> historyProvider) {
        this.pdfProvider = pdfProvider;
        this.historyProvider = historyProvider;
    }

    @Loggable(category = LogCollector.Category.FACTORY, withResult = true, description = "Document creation")
    @Override
    public Document createDocument() {
        PdfDocument doc = pdfProvider.getObject();
        doc.setHistoryLogger(historyProvider.getObject());
        return doc;
    }

    @Override
    public String getName() {
        return "PDF";
    }
}