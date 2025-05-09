package ru.doc.history;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.doc.memento.DocumentMemento;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Component
@Scope("prototype")
public class DocumentHistoryLogger {

    private final Stack<DocumentMemento> undoStack = new Stack<>();
    private final Stack<DocumentMemento> redoStack = new Stack<>();

    public void addMemento(DocumentMemento memento) {
        if (memento == null) return;
        undoStack.push(memento);
        redoStack.clear();
    }

    public boolean canUndo() {
        return undoStack.size() > 1;
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public DocumentMemento undo(DocumentMemento current) {
        if (undoStack.size() <= 1) return null;
        redoStack.push(undoStack.pop());
        return undoStack.peek();
    }

    public DocumentMemento redo(DocumentMemento current) {
        if (redoStack.isEmpty()) return null;
        DocumentMemento memento = redoStack.pop();
        undoStack.push(memento);
        return memento;
    }

    public List<DocumentMemento> getHistory() {
        return new ArrayList<>(undoStack);
    }

    public DocumentMemento getMementoAt(int index) {
        if (index < 0 || index >= undoStack.size()) return null;
        return undoStack.get(index);
    }

    public void clearRedo() {
        redoStack.clear();
    }

    public void saveHistoryToFile(File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new ArrayList<>(undoStack));
        }
    }

    @SuppressWarnings("unchecked")
    public void loadHistoryFromFile(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<DocumentMemento> list = (List<DocumentMemento>) ois.readObject();
            undoStack.clear();
            redoStack.clear();
            undoStack.addAll(list);
        }
    }
}