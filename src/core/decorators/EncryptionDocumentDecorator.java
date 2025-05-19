package core.decorators;

import core.documents.Document;

public class EncryptionDocumentDecorator extends DocumentDecorator {

    public EncryptionDocumentDecorator(Document document) {
        super(document);
    }

    @Override
    public void setContent(String content) {
        String encrypted = encrypt(content);
        super.setContent(encrypted);
    }

    @Override
    public String getContent() {
        String encrypted = super.getContent();
        return decrypt(encrypted);
    }

    private String encrypt(String input) {
        if (input == null) return null;
        System.out.println("EncryptionDecorator: Шифрование данных...");
        return new StringBuilder(input).reverse().toString();
    }

    private String decrypt(String input) {
        if (input == null) return null;
        System.out.println("EncryptionDecorator: Дешифрование данных...");
        return new StringBuilder(input).reverse().toString();
    }
}