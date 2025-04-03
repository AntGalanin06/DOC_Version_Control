package memento;

public class TextDocumentMemento extends DocumentMemento {
    private final String text;

    public TextDocumentMemento(String text) {
        super("TextDocument");
        this.text = text;
    }

    public String getText() {
        return text;
    }
}