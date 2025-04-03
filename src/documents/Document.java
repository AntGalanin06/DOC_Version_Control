package documents;

import memento.DocumentMemento;

public abstract class Document {
    public abstract String getContent();
    public abstract void setContent(String content);
    protected abstract DocumentMemento createMemento();
    protected abstract void restoreFromMemento(DocumentMemento memento);
}
