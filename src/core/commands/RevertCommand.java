package core.commands;

import core.documents.Document;
import core.memento.DocumentHistoryLogger;
import core.memento.DocumentMemento;

public class RevertCommand implements DocumentCommand {

    private final Document document;
    private final DocumentHistoryLogger historyLogger;
    private DocumentMemento savedCurrent;

    public RevertCommand(Document document, DocumentHistoryLogger historyLogger) {
        this.document = document;
        this.historyLogger = historyLogger;
    }

    @Override
    public void execute() {
        savedCurrent = document.createMemento();
        DocumentMemento target = historyLogger.undo(savedCurrent);
        if (target != null) document.restoreFromMemento(target);
    }

    @Override
    public void undo() {
        if (savedCurrent == null) return;
        DocumentMemento back = document.createMemento();
        historyLogger.addMemento(back);
        document.restoreFromMemento(savedCurrent);
        savedCurrent = null;
    }
}