package core.observer;

import core.documents.Document;

public class ConsoleLoggerObserver implements DocumentObserver {
    @Override
    public void update(Document document) {
        System.out.println("ConsoleLoggerObserver: " + document.getContent());
    }
}