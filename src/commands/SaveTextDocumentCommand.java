package commands;

import documents.TextDocument;
import memento.DocumentHistoryLogger;
import memento.DocumentMemento;
import memento.TextDocumentMemento;

public class SaveTextDocumentCommand implements DocumentCommand {
    private TextDocument document;
    private DocumentHistoryLogger historyLogger;

    public SaveTextDocumentCommand(TextDocument document, DocumentHistoryLogger historyLogger) {
        this.document = document;
        this.historyLogger = historyLogger;
    }

    @Override
    public void execute() {
        historyLogger.addMemento(new TextDocumentMemento(document.getContent()));
    }

    @Override
    public void undo() {
        DocumentMemento memento = historyLogger.undo();
        if(memento != null) {
            document.restoreFromMemento(memento);
        }
    }
}
