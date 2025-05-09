package ru.doc.commands;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.doc.document.Document;
import ru.doc.history.DocumentHistoryLogger;
import ru.doc.memento.DocumentMemento;
import ru.doc.logging.Loggable;
import ru.doc.logging.LogCollector;

@Component
@Scope("prototype")
@Loggable(category = LogCollector.Category.HISTORY)
public class UndoCommand implements DocumentCommand {
    private final Document document;
    private final DocumentHistoryLogger logger;

    public UndoCommand(Document document, DocumentHistoryLogger logger) {
        this.document = document;
        this.logger = logger;
    }

    @Override
    public void execute() {
        DocumentMemento current = document.createMemento();
        DocumentMemento target = logger.undo(current);
        if (target != null) {
            document.restoreFromMemento(target);
        }
    }

    @Override
    public void undo() {
        DocumentMemento current = document.createMemento();
        DocumentMemento target = logger.redo(current);
        if (target != null) {
            document.restoreFromMemento(target);
        }
    }
}