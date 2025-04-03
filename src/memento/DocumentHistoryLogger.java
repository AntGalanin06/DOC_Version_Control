package memento;

import java.util.Stack;

public class DocumentHistoryLogger {
    private Stack<DocumentMemento> history = new Stack<>();
    public void addMemento(DocumentMemento memento) {
        history.push(memento);
    }
    public DocumentMemento undo() {
        return history.isEmpty() ? null : history.pop();
    }
    public Stack<DocumentMemento> getHistory() {
        return history;
    }
}
