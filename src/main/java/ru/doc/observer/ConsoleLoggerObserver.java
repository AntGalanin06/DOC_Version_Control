package ru.doc.observer;

import ru.doc.document.Document;

public class ConsoleLoggerObserver implements DocumentObserver {
    @Override
    public void update(Document document) {
        System.out.println("ConsoleLoggerObserver: Document has been updated. New content:");
        System.out.println(">>> " + document.getContent());
    }
}