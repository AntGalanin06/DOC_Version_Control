package main;

import commands.*;
import decorators.EncryptionDocumentDecorator;
import decorators.LoggingDocumentDecorator;
import documents.Document;
import factory.*;
import logger.LogCollector;
import memento.DocumentHistoryLogger;
import memento.DocumentMemento;
import observer.ConsoleLoggerObserver;
import observer.DocumentObserver;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.stream.IntStream;

public class DocumentEditorGUI extends JFrame implements DocumentObserver {

    private JTextArea textArea;
    private JTextArea logArea;
    private DefaultListModel<String> historyModel;
    private JButton undoBtn;
    private JButton redoBtn;

    private Document              document;
    private DocumentHistoryLogger logger;

    public DocumentEditorGUI() {
        init();
        setVisible(true);
    }

    private void init() {
        String[] kinds = {"Text", "PDF", "Spreadsheet"};
        String   kind  = (String) JOptionPane.showInputDialog(
                null, "Тип документа", "Новый документ",
                JOptionPane.QUESTION_MESSAGE, null, kinds, kinds[0]);
        if (kind == null) System.exit(0);

        DocumentFactory factory = switch (kind) {
            case "PDF"         -> new PdfDocumentFactory();
            case "Spreadsheet" -> new SpreadsheetDocumentFactory();
            default            -> new TextDocumentFactory();
        };

        logger   = new DocumentHistoryLogger();
        document = factory.createDocument();
        document.setHistoryLogger(logger);
        document.addObserver(this);
        document.addObserver(new ConsoleLoggerObserver());

        JCheckBox logCB = new JCheckBox("Логирование", true);
        JCheckBox encCB = new JCheckBox("Шифрование",  true);
        JPanel opts = new JPanel(new GridLayout(0,1));
        opts.add(logCB); opts.add(encCB);
        if (JOptionPane.showConfirmDialog(
                null, opts, "Декораторы",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) System.exit(0);

        if (encCB.isSelected()) document = new EncryptionDocumentDecorator(document);
        if (logCB.isSelected()) document = new LoggingDocumentDecorator(document);

        logger.addMemento(document.createMemento());

        buildUI(kind);
    }

    private void buildUI(String kind) {
        setTitle(kind + " editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(950, 520));

        JTabbedPane tabs = new JTabbedPane();

        JPanel docPanel = new JPanel(new BorderLayout());

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        docPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        historyModel = new DefaultListModel<>();
        JList<String> historyList = new JList<>(historyModel);
        historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int idx = historyList.getSelectedIndex();
                if (idx >= 0) jumpToHistory(idx);
            }
        });
        docPanel.add(new JScrollPane(historyList), BorderLayout.EAST);

        JPanel buttons = new JPanel(new FlowLayout());
        JButton editBtn      = new JButton("Edit");
        undoBtn              = new JButton("Undo");
        redoBtn              = new JButton("Redo");
        JButton saveHistBtn  = new JButton("SaveHistory");
        JButton loadHistBtn  = new JButton("LoadHistory");

        editBtn.addActionListener(e -> editContent());
        undoBtn.addActionListener(e -> { new UndoCommand(document, logger).execute();  refresh(); });
        redoBtn.addActionListener(e -> { new RedoCommand(document, logger).execute();  refresh(); });
        saveHistBtn.addActionListener(e -> saveHistoryToFile());
        loadHistBtn.addActionListener(e -> loadHistoryFromFile());

        buttons.add(editBtn);
        buttons.add(undoBtn); buttons.add(redoBtn);
        buttons.add(saveHistBtn); buttons.add(loadHistBtn);

        docPanel.add(buttons, BorderLayout.SOUTH);
        tabs.add("Document", docPanel);

        logArea = new JTextArea();  logArea.setEditable(false);
        tabs.add("Logs", new JScrollPane(logArea));

        add(tabs, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        refresh();
    }

    private void editContent() {
        String current = document.getContent();
        String newText = JOptionPane.showInputDialog(this, "Текст:", current);
        if (newText != null && !newText.equals(current)) {
            new ChangeTextCommand(document, newText).execute();
            refresh();
        }
    }

    private void jumpToHistory(int index) {
        DocumentMemento current = document.createMemento();
        DocumentMemento target  = logger.getMementoAt(index);
        if (target != null) {
            document.restoreFromMemento(target);
            logger.addMemento(current);
            logger.clearRedo();
            refresh();
        }
    }

    private void saveHistoryToFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                logger.addMemento(document.createMemento());
                logger.saveHistoryToFile(fc.getSelectedFile());
                JOptionPane.showMessageDialog(this, "История сохранена");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка сохранения: " + ex.getMessage());
            }
        }
    }

    private void loadHistoryFromFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                logger.loadHistoryFromFile(fc.getSelectedFile());
                refresh();
                JOptionPane.showMessageDialog(this, "История загружена");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка загрузки: " + ex.getMessage());
            }
        }
    }

    private void refresh() {
        historyModel.clear();
        IntStream.range(0, logger.getHistory().size())
                .forEach(i -> historyModel.addElement("#" + i));
        undoBtn.setEnabled(logger.canUndo());
        redoBtn.setEnabled(logger.canRedo());
        textArea.setText(document.getContent());
        logArea.setText(String.join("\n", LogCollector.getInstance().getLogs()));
    }

    @Override
    public void update(Document d) {
        if (d == document) textArea.setText(d.getContent());
    }
}