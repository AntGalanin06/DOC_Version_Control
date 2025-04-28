package observer;

import documents.Document;

public class ConsoleLoggerObserver implements DocumentObserver {
    @Override
    public void update(Document document) {
        System.out.println("ConsoleLoggerObserver: Документ был обновлен. Новое содержимое:");
        System.out.println(">>> " + document.getContent());
    }
}