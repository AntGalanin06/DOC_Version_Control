package observer;

import commands.SaveTextDocumentCommand;
import documents.Document;

public class ConcreteDocumentObserver implements DocumentObserver {
    private SaveTextDocumentCommand saveCommand;

    public ConcreteDocumentObserver(SaveTextDocumentCommand saveCommand) {
        this.saveCommand = saveCommand;
    }

    @Override
    public void update(Document document) {
        saveCommand.execute();
    }
}
