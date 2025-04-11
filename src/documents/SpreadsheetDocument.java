package documents;

import memento.DocumentMemento;
import memento.SpreadsheetDocumentMemento;

public class SpreadsheetDocument extends Document {
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
        return new SpreadsheetDocumentMemento(content);
    }

    @Override
    public void restoreFromMemento(DocumentMemento memento) {
        if(memento instanceof SpreadsheetDocumentMemento) {
            this.content = ((SpreadsheetDocumentMemento)memento).getText();
        }
    }
}