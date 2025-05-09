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
public class RevertCommand implements DocumentCommand {
    private final Document document;
    private final DocumentHistoryLogger historyLogger;
    private final int targetIndex;
    private DocumentMemento savedCurrent;

    public RevertCommand(Document document, DocumentHistoryLogger historyLogger, int targetIndex) {
        this.document = document;
        this.historyLogger = historyLogger;
        this.targetIndex = targetIndex;
    }

    @Override
    public void execute() {
        savedCurrent = document.createMemento();
        DocumentMemento target = historyLogger.getMementoAt(targetIndex);
        if (target != null) {
            document.restoreFromMemento(target);
        }
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