package ru.doc.decorators;

import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.doc.logging.Loggable;
import ru.doc.logging.LogCollector;
import ru.doc.document.Document;

@Component
@Scope("prototype")
public class EncryptionDocumentDecorator extends DocumentDecorator {
    public EncryptionDocumentDecorator(Document document) {
        super(document);
    }

    @Override
    public void setContent(String content) {
        EncryptionDocumentDecorator proxy = (EncryptionDocumentDecorator) AopContext.currentProxy();
        String encrypted = proxy.encrypt(content);
        super.setContent(encrypted);
    }

    @Override
    public String getContent() {
        String encrypted = super.getContent();
        EncryptionDocumentDecorator proxy = (EncryptionDocumentDecorator) AopContext.currentProxy();
        return proxy.decrypt(encrypted);
    }

    @Loggable(category = LogCollector.Category.DECORATOR, level = LogCollector.Level.DEBUG, withTime = true, description = "Data encryption")
    public String encrypt(String input) {
        if (input == null) return null;
        return new StringBuilder(input).reverse().toString();
    }

    @Loggable(category = LogCollector.Category.DECORATOR, level = LogCollector.Level.DEBUG, withTime = true, description = "Data decryption")
    public String decrypt(String input) {
        if (input == null) return null;
        return new StringBuilder(input).reverse().toString();
    }
}