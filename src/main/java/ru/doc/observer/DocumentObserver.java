package ru.doc.observer;

import ru.doc.document.Document;

public interface DocumentObserver {
    void update(Document document);
}