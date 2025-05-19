package ru.doc.gui;

import ru.doc.commands.ChangeTextCommand;
import ru.doc.commands.RedoCommand;
import ru.doc.commands.UndoCommand;
import ru.doc.commands.RevertCommand;
import ru.doc.decorators.EncryptionDocumentDecorator;
import ru.doc.document.Document;
import ru.doc.factory.DocumentFactory;
import ru.doc.logging.LogCollector;
import ru.doc.history.DocumentHistoryLogger;
import ru.doc.memento.DocumentMemento;
import ru.doc.observer.ConsoleLoggerObserver;
import ru.doc.observer.DocumentObserver;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.stream.IntStream;

import jakarta.annotation.PostConstruct;

@Component
public class DocumentEditorGUI extends JFrame implements DocumentObserver {

    private final List<DocumentFactory> factories;
    private final LogCollector logCollector;
    private final ObjectProvider<DocumentHistoryLogger> historyLoggerProvider;
    private final ApplicationContext context;

    private JTextArea textArea;
    private JTextArea logArea;
    private DefaultListModel<String> historyModel;
    private JButton undoBtn;
    private JButton redoBtn;

    private Document document;
    private DocumentHistoryLogger logger;

    private JRadioButton allBtn, infoBtn, debugBtn, errorBtn;
    private JRadioButton allCatBtn, factCatBtn, decCatBtn;

    @Autowired
    public DocumentEditorGUI(List<DocumentFactory> factories,
                             LogCollector logCollector,
                             ObjectProvider<DocumentHistoryLogger> historyLoggerProvider,
                             ApplicationContext context) {
        this.factories = factories;
        this.logCollector = logCollector;
        this.historyLoggerProvider = historyLoggerProvider;
        this.context = context;
    }

    @PostConstruct
    public void initialize() {
        SwingUtilities.invokeLater(() -> {
            try {
                init();
                setVisible(true);
            } catch (Exception e) {
                logCollector.add(LogCollector.Level.ERROR,
                        LogCollector.Category.UI,
                        LogCollector.OperationType.ERROR,
                        "Initialization error: " + e.getMessage());
                JOptionPane.showMessageDialog(null,
                        "Error: " + e.getMessage(),
                        "Initialization Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void init() {
        String[] names = factories.stream()
                .map(DocumentFactory::getName)
                .toArray(String[]::new);

        String kind = (String) JOptionPane.showInputDialog(
                null, "Document type", "New document",
                JOptionPane.QUESTION_MESSAGE, null, names, names[0]);

        if (kind == null) System.exit(0);

        DocumentFactory factory = factories.stream()
                .filter(f -> f.getName().equals(kind))
                .findFirst()
                .orElseThrow();

        logger = historyLoggerProvider.getObject();
        document = factory.createDocument();
        document.setHistoryLogger(logger);
        document.addObserver(this);
        document.addObserver(new ConsoleLoggerObserver());

        JCheckBox encCB = new JCheckBox("Encryption", true);
        JPanel opts = new JPanel(new GridLayout(0, 1));
        opts.add(encCB);

        if (JOptionPane.showConfirmDialog(null, opts,
                "Decorators", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
            System.exit(0);
        }
        if (encCB.isSelected()) {
            document = context.getBean(EncryptionDocumentDecorator.class, document);
        }

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

        JPopupMenu historyMenu = new JPopupMenu();
        JMenuItem revertItem = new JMenuItem("Revert");
        historyMenu.add(revertItem);

        historyList.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed  (MouseEvent e) { maybeShowMenu(e); }
            @Override public void mouseReleased (MouseEvent e) { maybeShowMenu(e); }
            @Override public void mouseClicked  (MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int idx = historyList.locationToIndex(e.getPoint());
                    if (idx >= 0) {
                        new RevertCommand(document, logger, idx).execute();
                        logger.addMemento(document.createMemento());
                        logger.clearRedo();
                        refresh();
                    }
                }
            }
            private void maybeShowMenu(MouseEvent e) {
                if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                    int idx = historyList.locationToIndex(e.getPoint());
                    if (idx >= 0) {
                        historyList.setSelectedIndex(idx);
                        historyMenu.show(historyList, e.getX(), e.getY());
                    }
                }
            }
        });

        revertItem.addActionListener(e -> {
            int idx = historyList.getSelectedIndex();
            if (idx >= 0) {
                new RevertCommand(document, logger, idx).execute();
                logger.addMemento(document.createMemento());
                logger.clearRedo();
                refresh();
            }
        });

        docPanel.add(new JScrollPane(historyList), BorderLayout.EAST);

        JPanel buttons = new JPanel(new FlowLayout());
        JButton editBtn      = new JButton("Edit");
        undoBtn              = new JButton("Undo");
        redoBtn              = new JButton("Redo");
        JButton saveHistBtn  = new JButton("Save history");
        JButton loadHistBtn  = new JButton("Load history");

        editBtn.addActionListener(e -> editContent());
        undoBtn.addActionListener(e -> { new UndoCommand(document, logger).execute();  refresh(); });
        redoBtn.addActionListener(e -> { new RedoCommand(document, logger).execute();  refresh(); });
        saveHistBtn.addActionListener(e -> saveHistoryToFile());
        loadHistBtn.addActionListener(e -> loadHistoryFromFile());

        buttons.add(editBtn);
        buttons.add(undoBtn);
        buttons.add(redoBtn);
        buttons.add(saveHistBtn);
        buttons.add(loadHistBtn);

        docPanel.add(buttons, BorderLayout.SOUTH);

        tabs.add("Document", docPanel);

        logArea = new JTextArea();
        logArea.setEditable(false);

        JPanel logPanel = new JPanel(new BorderLayout());

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup levelGroup = new ButtonGroup();
        allBtn   = new JRadioButton("ALL",   true);
        infoBtn  = new JRadioButton("INFO");
        debugBtn = new JRadioButton("DEBUG");
        errorBtn = new JRadioButton("ERROR");
        levelGroup.add(allBtn); levelGroup.add(infoBtn);
        levelGroup.add(debugBtn); levelGroup.add(errorBtn);
        filterPanel.add(new JLabel("Filter level:"));
        filterPanel.add(allBtn); filterPanel.add(infoBtn);
        filterPanel.add(debugBtn); filterPanel.add(errorBtn);

        allBtn  .addActionListener(e -> refreshLogs());
        infoBtn .addActionListener(e -> refreshLogs());
        debugBtn.addActionListener(e -> refreshLogs());
        errorBtn.addActionListener(e -> refreshLogs());

        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup catGroup = new ButtonGroup();
        allCatBtn  = new JRadioButton("All Categories", true);
        factCatBtn = new JRadioButton("FACTORY");
        decCatBtn  = new JRadioButton("DECORATOR");
        catGroup.add(allCatBtn); catGroup.add(factCatBtn); catGroup.add(decCatBtn);
        categoryPanel.add(new JLabel("Filter category:"));
        categoryPanel.add(allCatBtn); categoryPanel.add(factCatBtn);
        categoryPanel.add(decCatBtn);

        allCatBtn .addActionListener(e -> refreshLogs());
        factCatBtn.addActionListener(e -> refreshLogs());
        decCatBtn .addActionListener(e -> refreshLogs());

        JPanel filtersContainer = new JPanel(new BorderLayout());
        filtersContainer.add(filterPanel, BorderLayout.NORTH);
        filtersContainer.add(categoryPanel, BorderLayout.CENTER);

        logPanel.add(filtersContainer, BorderLayout.NORTH);
        logPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);

        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton exportBtn = new JButton("Export logs");
        exportBtn.setPreferredSize(new Dimension(160, 30));
        exportBtn.addActionListener(e -> exportLogsToFile());
        exportPanel.add(exportBtn);

        logPanel.add(exportPanel, BorderLayout.SOUTH);

        tabs.add("Logs", logPanel);

        add(tabs, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        refresh();
    }

    private void editContent() {
        String current = document.getContent();
        String newText = JOptionPane.showInputDialog(this, "Text:", current);
        if (newText != null && !newText.equals(current)) {
            new ChangeTextCommand(document, newText).execute();
            logger.addMemento(document.createMemento());
            logger.clearRedo();
            refresh();
        }
    }

    private void jumpToHistory(int index) {
        DocumentMemento current = document.createMemento();
        DocumentMemento target  = logger.getMementoAt(index);
        if (target != null) {
            new RevertCommand(document, logger, index).execute();
            logger.addMemento(current);
            logger.clearRedo();
            refresh();
        }
    }

    private void saveHistoryToFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                logger.saveHistoryToFile(fc.getSelectedFile());
                JOptionPane.showMessageDialog(this, "History saved");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Save error: " + ex.getMessage());
            }
        }
    }

    private void loadHistoryFromFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                logger.loadHistoryFromFile(fc.getSelectedFile());
                if (!logger.getHistory().isEmpty()) {
                    DocumentMemento last =
                            logger.getHistory().get(logger.getHistory().size() - 1);
                    document.restoreFromMemento(last);
                }
                refresh();
                JOptionPane.showMessageDialog(this, "History loaded");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Load error: " + ex.getMessage());
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
        refreshLogs();
    }

    private void refreshLogs() {
        List<String> logs;
        if (allBtn.isSelected()) {
            logs = allCatBtn.isSelected()
                    ? logCollector.getRecords()
                    : logCollector.getRecordsByCategory(selectedCategory());
        } else {
            LogCollector.Level lvl = selectedLevel();
            logs = allCatBtn.isSelected()
                    ? logCollector.getRecordsByLevel(lvl)
                    : logCollector.getRecordsByCategoryAndLevel(selectedCategory(), lvl);
        }
        logArea.setText(String.join("\n", logs));
    }

    private LogCollector.Level selectedLevel() {
        return infoBtn.isSelected()  ? LogCollector.Level.INFO
                : debugBtn.isSelected() ? LogCollector.Level.DEBUG
                :                        LogCollector.Level.ERROR;
    }

    private LogCollector.Category selectedCategory() {
        return factCatBtn.isSelected() ? LogCollector.Category.FACTORY
                : LogCollector.Category.DECORATOR;
    }

    private void exportLogsToFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter writer =
                         new BufferedWriter(new FileWriter(fc.getSelectedFile()))) {
                for (String record : logArea.getText().split("\n")) {
                    writer.write(record);
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(this, "Logs exported");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Export error: " + ex.getMessage());
            }
        }
    }

    @Override
    public void update(Document doc) {
        if (doc == document) textArea.setText(doc.getContent());
    }
}