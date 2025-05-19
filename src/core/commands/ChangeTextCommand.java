package core.commands;

import core.documents.Document;

public class ChangeTextCommand implements DocumentCommand {
    private Document document;
    private String newText;
    private String previousText;

    public ChangeTextCommand(Document document, String newText) {
        this.document = document;
        this.newText = newText;
    }

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