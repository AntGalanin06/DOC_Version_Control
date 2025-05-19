package gui;

import core.logger.LogCollector;

import javax.swing.*;
import java.util.List;

public class LogsPanel extends JScrollPane {

    private final JTextArea area = new JTextArea();

    public LogsPanel() {
        setViewportView(area);
        area.setEditable(false);
        new Timer(500, e -> refresh()).start();
    }

    private void refresh() {
        List<String> logs = LogCollector.getInstance().getLogs();
        StringBuilder sb = new StringBuilder();
        for (String s : logs) sb.append(s).append('\n');
        area.setText(sb.toString());
        area.setCaretPosition(area.getDocument().getLength());
    }
}