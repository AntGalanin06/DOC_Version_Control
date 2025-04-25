package commands;

import documents.Document;
import memento.DocumentHistoryLogger;
import memento.DocumentMemento;

public class RedoCommand implements DocumentCommand {

    private final Document document;
    private final DocumentHistoryLogger logger;

    public RedoCommand(Document document, DocumentHistoryLogger logger) {
        this.document = document;
        this.logger   = logger;
    }

    @Override
    public void execute() {
        DocumentMemento current = document.createMemento();
        DocumentMemento target  = logger.redo(current);
        if (target != null) document.restoreFromMemento(target);
    }

    @Override
    public void undo() {
        DocumentMemento current = document.createMemento();
        DocumentMemento target  = logger.undo(current);
        if (target != null) document.restoreFromMemento(target);
    }
}