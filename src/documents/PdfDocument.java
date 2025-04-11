package documents;

import memento.DocumentMemento;
import memento.PdfDocumentMemento;

public class PdfDocument extends Document {
    private String content = "";

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    protected DocumentMemento createMemento() {
        return new PdfDocumentMemento(content);
    }

    @Override
    public void restoreFromMemento(DocumentMemento memento) {
        if(memento instanceof PdfDocumentMemento) {
            this.content = ((PdfDocumentMemento)memento).getText();
        }
    }
}