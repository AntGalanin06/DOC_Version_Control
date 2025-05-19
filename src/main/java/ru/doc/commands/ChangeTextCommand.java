package ru.doc.commands;

import ru.doc.document.Document;

public class ChangeTextCommand implements DocumentCommand {

    private final Document document;
    private final String   newText;
    private String         previousText;

    public ChangeTextCommand(Document document, String newText) {
        this.document = document;
        this.newText  = newText;
    }

    @Override
    public void execute() {
        previousText = document.getContent();
        document.setContent(newText);
    }

    @Override
    public void undo() {
        if (previousText != null) {
            document.setContent(previousText);
        }
    }
}