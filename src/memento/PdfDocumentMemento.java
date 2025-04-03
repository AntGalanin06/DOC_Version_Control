package memento;

public class PdfDocumentMemento extends DocumentMemento {
    private final String text;

    public PdfDocumentMemento(String text) {
        super("PdfDocument");
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
