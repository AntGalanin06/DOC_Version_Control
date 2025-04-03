package memento;

public class SpreadsheetDocumentMemento extends DocumentMemento {
    private final String text;

    public SpreadsheetDocumentMemento(String text) {
        super("SpreadsheetDocument");
        this.text = text;
    }

    public String getText() {
        return text;
    }
}