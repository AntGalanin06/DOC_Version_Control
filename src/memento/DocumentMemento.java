package memento;

public abstract class DocumentMemento {
    private final String type;
    protected DocumentMemento(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
