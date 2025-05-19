package core.observer;

import core.documents.Document;

public interface DocumentObserver {
    void update(Document document);
}