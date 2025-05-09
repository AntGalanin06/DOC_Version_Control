package ru.doc.factory;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.doc.document.Document;
import ru.doc.document.SpreadsheetDocument;
import ru.doc.history.DocumentHistoryLogger;
import ru.doc.logging.Loggable;
import ru.doc.logging.LogCollector;

@Component
public class SpreadsheetDocumentFactory implements DocumentFactory {
    private final ObjectProvider<SpreadsheetDocument> sheetProvider;
    private final ObjectProvider<DocumentHistoryLogger> historyProvider;

    @Autowired
    public SpreadsheetDocumentFactory(ObjectProvider<SpreadsheetDocument> sheetProvider,
                                      ObjectProvider<DocumentHistoryLogger> historyProvider) {
        this.sheetProvider = sheetProvider;
        this.historyProvider = historyProvider;
    }

    @Loggable(category = LogCollector.Category.FACTORY, withResult = true, description = "Document creation")
    @Override
    public Document createDocument() {
        SpreadsheetDocument doc = sheetProvider.getObject();
        doc.setHistoryLogger(historyProvider.getObject());
        return doc;
    }

    @Override
    public String getName() {
        return "Spreadsheet";
    }
}