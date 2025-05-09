package ru.doc.document;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.doc.logging.Loggable;
import ru.doc.logging.LogCollector;

@Component
@Scope("prototype")
public class TextDocument extends Document {
    @Loggable(category = LogCollector.Category.DOCUMENT, withArgs = true)
    @Override
    public void setContent(String content) {
        super.setContent(content);
    }

    @Loggable(category = LogCollector.Category.DOCUMENT, withResult = true)
    @Override
    public String getContent() {
        return super.getContent();
    }
}