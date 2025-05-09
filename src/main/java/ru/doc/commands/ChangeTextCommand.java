package ru.doc.commands;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.doc.document.Document;
import ru.doc.logging.Loggable;
import ru.doc.logging.LogCollector;

@Component
@Scope("prototype")
public class ChangeTextCommand implements DocumentCommand {
    private final Document document;
    private final String newText;
    private String previousText;

    public ChangeTextCommand(Document document, String newText) {
        this.document = document;
        this.newText = newText;
    }

    @Loggable(category = LogCollector.Category.HISTORY)
    @Override
    public void execute() {
        previousText = document.getContent();
        document.setContent(newText);
    }

    @Override
    public void undo() {
        document.setContent(previousText);
    }
}