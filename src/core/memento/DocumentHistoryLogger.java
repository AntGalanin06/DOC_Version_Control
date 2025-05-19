package core.memento;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DocumentHistoryLogger {

    private final Stack<DocumentMemento> undoStack = new Stack<>();
    private final Stack<DocumentMemento> redoStack = new Stack<>();

    public synchronized void addMemento(DocumentMemento memento) {
        if (memento == null) return;
        undoStack.push(memento);
        redoStack.clear();
    }

    public synchronized boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public synchronized boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public synchronized DocumentMemento undo(DocumentMemento current) {
        if (!canUndo()) return null;
        if (current != null) redoStack.push(current);
        return undoStack.pop();
    }

    public synchronized DocumentMemento redo(DocumentMemento current) {
        if (!canRedo()) return null;
        if (current != null) undoStack.push(current);
        return redoStack.pop();
    }

    public synchronized List<DocumentMemento> getHistory() {
        return new ArrayList<>(undoStack);
    }

    public synchronized DocumentMemento getMementoAt(int index) {
        if (index < 0 || index >= undoStack.size()) return null;
        return undoStack.get(index);
    }

    public synchronized void clearRedo() {
        redoStack.clear();
    }

    public synchronized void saveHistoryToFile(File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new ArrayList<>(undoStack));
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized void loadHistoryFromFile(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<DocumentMemento> list = (List<DocumentMemento>) ois.readObject();
            undoStack.clear();
            redoStack.clear();
            undoStack.addAll(list);
        }
    }
}