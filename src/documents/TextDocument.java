package documents;

import memento.DocumentMemento;
import memento.TextDocumentMemento;

public class TextDocument extends Document {
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
        return new TextDocumentMemento(content);
    }

    @Override
    protected void restoreFromMemento(DocumentMemento memento) {
        if(memento instanceof TextDocumentMemento) {
            this.content = ((TextDocumentMemento) memento).getText();
        }
    }
}