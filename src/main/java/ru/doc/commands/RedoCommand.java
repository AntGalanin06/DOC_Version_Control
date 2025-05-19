package ru.doc.commands;

import ru.doc.document.Document;
import ru.doc.history.DocumentHistoryLogger;
import ru.doc.memento.DocumentMemento;

public class RedoCommand implements DocumentCommand {

    private final Document              document;
    private final DocumentHistoryLogger history;
    private DocumentMemento             restoredFrom;

    public RedoCommand(Document document, DocumentHistoryLogger history) {
        this.document = document;
        this.history  = history;
    }

    @Override
    public void execute() {
        DocumentMemento current = document.createMemento();
        DocumentMemento next = history.redo(current);
        if (next != null) {
            document.restoreFromMemento(next);
            restoredFrom = current;                 // нужно для обратного шага
        }
    }

    @Override
    public void undo() {                            // «откатываем повторное применённое»
        if (restoredFrom != null) {
            history.addMemento(document.createMemento());
            document.restoreFromMemento(restoredFrom);
            restoredFrom = null;
        }
    }
}