package ru.doc.commands;

import ru.doc.document.Document;
import ru.doc.history.DocumentHistoryLogger;
import ru.doc.memento.DocumentMemento;

public class RevertCommand implements DocumentCommand {

    private final Document              document;
    private final DocumentHistoryLogger history;
    private final int                   targetIndex;

    private DocumentMemento backup;

    public RevertCommand(Document document,
                         DocumentHistoryLogger history,
                         int targetIndex) {
        this.document    = document;
        this.history     = history;
        this.targetIndex = targetIndex;
    }

    @Override
    public void execute() {
        backup = document.createMemento();
        DocumentMemento target = history.getMementoAt(targetIndex);
        if (target != null) {
            document.restoreFromMemento(target);
        }
    }

    @Override
    public void undo() {
        if (backup != null) {
            history.addMemento(document.createMemento());
            document.restoreFromMemento(backup);
            backup = null;
        }
    }
}