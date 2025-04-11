package decorators;

import documents.Document;

public class LoggingDocumentDecorator extends DocumentDecorator {

    public LoggingDocumentDecorator(Document document) {
        super(document);
    }

    @Override
    public void setContent(String content) {
        System.out.println("Logging: изменение содержимого.");
        super.setContent(content);
    }
}
