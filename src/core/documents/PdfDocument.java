package core.documents;

import core.memento.DocumentMemento;
import core.memento.GenericDocumentMemento;
import java.util.Map;

public class PdfDocument extends Document {

    private volatile String content = "";

    @Override
    public synchronized String getContent() {
        return content;
    }

    @Override
    public synchronized void setContent(String content) {
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
        synchronized (this) {
            if (!content.equals(restored)) {
                content = restored;
                notifyObservers();
            }
        }
    }
}