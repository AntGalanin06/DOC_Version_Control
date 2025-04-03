package factory;

import documents.Document;

public interface DocumentFactory {
    Document createDocument(String type);
}
