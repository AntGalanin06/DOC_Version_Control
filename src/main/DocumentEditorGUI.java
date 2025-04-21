package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import commands.ChangeTextCommand;
import commands.RevertCommand;
import memento.DocumentHistoryLogger;
import documents.TextDocument;

public class DocumentEditorGUI extends JFrame {

    private TextDocument document;
    private DocumentHistoryLogger historyLogger;
    private JTextArea textArea;

    public DocumentEditorGUI() {
        document = new TextDocument();
        historyLogger = new DocumentHistoryLogger();
        // Сохраняем первоначальное состояние документа
        historyLogger.addMemento(document.createMemento());
        initUI();
    }

    private void initUI() {
        setTitle("Редактор документа");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea(20, 40);
        textArea.setText(document.getContent());
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel(new FlowLayout());
        JButton editButton = new JButton("Редактировать");
        JButton revertButton = new JButton("Откат");

        // Обработчик для редактирования документа
        editButton.addActionListener(e -> {
            String newText = JOptionPane.showInputDialog(DocumentEditorGUI.this,
                    "Введите новый текст:", document.getContent());
            if (newText != null) {
                // Сохраняем текущее состояние документа
                historyLogger.addMemento(document.createMemento());
                ChangeTextCommand changeCmd = new ChangeTextCommand(document, newText);
                changeCmd.execute();
                textArea.setText(document.getContent());
            }
        });

        // Обработчик для отката состояния документа
        revertButton.addActionListener(e -> {
            RevertCommand revertCmd = new RevertCommand(document, historyLogger);
            revertCmd.execute();
            textArea.setText(document.getContent());
        });

        panel.add(editButton);
        panel.add(revertButton);
        add(panel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }
}