package documents;

import memento.DocumentMemento;

public abstract class Document {
    public abstract String getContent();
    public abstract void setContent(String content);
    protected abstract DocumentMemento createMemento();
    public abstract void restoreFromMemento(DocumentMemento memento);
}
