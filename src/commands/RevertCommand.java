package commands;

import documents.Document;
import memento.DocumentHistoryLogger;
import memento.DocumentMemento;

public class RevertCommand implements DocumentCommand {
    private Document document;
    private DocumentHistoryLogger historyLogger;
    private DocumentMemento revertedState;

    public RevertCommand(Document document, DocumentHistoryLogger historyLogger) {
        this.document = document;
        this.historyLogger = historyLogger;
    }

    @Override
    public void execute() {
        revertedState = historyLogger.undo();
        if(revertedState != null) {
            document.restoreFromMemento(revertedState);
        }
    }

    @Override
    public void undo() {
        // Реализация отката операции отката, если требуется
    }
}
