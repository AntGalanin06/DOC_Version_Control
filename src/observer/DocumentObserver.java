package observer;

import documents.Document;

public interface DocumentObserver {
    void update(Document document);
}