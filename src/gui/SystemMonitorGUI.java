package gui;

import concurrency.engine.ConcurrencyEngine;
import core.decorators.EncryptionDocumentDecorator;
import core.decorators.LoggingDocumentDecorator;
import core.documents.TextDocument;
import core.memento.DocumentHistoryLogger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SystemMonitorGUI extends JFrame {

    private final ConcurrencyEngine engine;

    public SystemMonitorGUI(int c, int w, int cap, int total) {

        var baseDoc = new TextDocument();
        var encDoc  = new EncryptionDocumentDecorator(baseDoc);
        var logDoc  = new LoggingDocumentDecorator(encDoc);
        var history = new DocumentHistoryLogger();

        engine = new ConcurrencyEngine(c, w, cap, total, logDoc, history);

        setTitle("Concurrency Monitor");
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Queue", new QueueDashboard(engine.stats()));
        tabs.add("Logs", new LogsPanel());
        add(tabs, BorderLayout.CENTER);

        setSize(750, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { engine.shutdown(); }
        });
    }
}