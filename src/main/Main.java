package main;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DocumentEditorGUI editorGUI = new DocumentEditorGUI();
            editorGUI.setVisible(true);
        });
    }
}