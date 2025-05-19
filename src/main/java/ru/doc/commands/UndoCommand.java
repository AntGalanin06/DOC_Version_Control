package ru.doc.commands;

import ru.doc.document.Document;
import ru.doc.history.DocumentHistoryLogger;
import ru.doc.memento.DocumentMemento;

public class UndoCommand implements DocumentCommand {

    private final Document              document;
    private final DocumentHistoryLogger history;
    private DocumentMemento             restoredFrom;

    public UndoCommand(Document document, DocumentHistoryLogger history) {
        this.document = document;
        this.history  = history;
    }

    @Override
    public void execute() {
        DocumentMemento current = document.createMemento();
        DocumentMemento previous = history.undo(current);
        if (previous != null) {
            document.restoreFromMemento(previous);
            restoredFrom = current;                 // запоминаем, чтобы можно было вернуть
        }
    }

    @Override
    public void undo() {                            // «откатываем откат»
        if (restoredFrom != null) {
            history.addMemento(document.createMemento());
            document.restoreFromMemento(restoredFrom);
            restoredFrom = null;
        }
    }
}