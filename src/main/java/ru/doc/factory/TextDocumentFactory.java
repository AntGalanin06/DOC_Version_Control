package ru.doc.factory;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.doc.document.Document;
import ru.doc.document.TextDocument;
import ru.doc.history.DocumentHistoryLogger;
import ru.doc.logging.Loggable;
import ru.doc.logging.LogCollector;

@Component
public class TextDocumentFactory implements DocumentFactory {
    private final ObjectProvider<TextDocument> textProvider;
    private final ObjectProvider<DocumentHistoryLogger> historyProvider;

    @Autowired
    public TextDocumentFactory(ObjectProvider<TextDocument> textProvider,
                               ObjectProvider<DocumentHistoryLogger> historyProvider) {
        this.textProvider = textProvider;
        this.historyProvider = historyProvider;
    }

    @Loggable(category = LogCollector.Category.FACTORY, withResult = true, description = "Document creation")
    @Override
    public Document createDocument() {
        TextDocument doc = textProvider.getObject();
        doc.setHistoryLogger(historyProvider.getObject());
        return doc;
    }

    @Override
    public String getName() {
        return "Text";
    }
}