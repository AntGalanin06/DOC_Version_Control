package ru.doc.factory;

import ru.doc.document.Document;

public interface DocumentFactory {
    Document createDocument();
    String getName();
}