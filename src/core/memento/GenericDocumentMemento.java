package core.memento;

import java.io.Serial;
import java.util.Map;

public class GenericDocumentMemento implements DocumentMemento {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<String, Object> state;

    public GenericDocumentMemento(Map<String, Object> state) {
        this.state = Map.copyOf(state);
    }

    @Override
    public Map<String, Object> getState() {
        return state;
    }
}