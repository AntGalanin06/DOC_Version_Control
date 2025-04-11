package commands;

import documents.TextDocument;

public class ChangeTextCommand implements DocumentCommand {
    private TextDocument document;
    private String newText;
    private String previousText;

    public ChangeTextCommand(TextDocument document, String newText) {
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
