package documents;

import memento.DocumentMemento;
import memento.GenericDocumentMemento;

import java.util.Map;

public class TextDocument extends Document {

    private String content = "";

    @Override
    public String getContent() { return content; }

    @Override
    public void setContent(String content) {
        if (content == null) content = "";
        if (!this.content.equals(content)) {
            this.content = content;
            notifyObservers();
            if (historyLogger != null)
                historyLogger.addMemento(createMemento());
        }
    }

    @Override
    public DocumentMemento createMemento() {
        return new GenericDocumentMemento(Map.of("content", content));
    }

    @Override
    public void restoreFromMemento(DocumentMemento memento) {
        String restored = (String) memento.getState().getOrDefault("content", "");
        if (!content.equals(restored)) {
            content = restored;
            notifyObservers();
        }
    }
}