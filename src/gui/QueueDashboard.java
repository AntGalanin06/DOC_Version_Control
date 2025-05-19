package gui;

import concurrency.monitor.StatsAggregator;
import concurrency.model.Request;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QueueDashboard extends JPanel {

    private final StatsAggregator agg;
    private final JProgressBar bar = new JProgressBar();
    private final DefaultTableModel clientM = new DefaultTableModel(
            new String[]{"Client", "Produced", "Avg delay"}, 0);
    private final DefaultTableModel workerM = new DefaultTableModel(
            new String[]{"Worker", "Consumed", "State"}, 0);
    private final DefaultListModel<String> allM = new DefaultListModel<>();

    public QueueDashboard(StatsAggregator agg) {
        this.agg = agg;
        setLayout(new BorderLayout(8,8));
        bar.setStringPainted(true);
        add(bar, BorderLayout.NORTH);

        JTable cl = new JTable(clientM);
        JTable wk = new JTable(workerM);
        JSplitPane tables = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(cl),
                new JScrollPane(wk));
        tables.setResizeWeight(0.5);

        JScrollPane allPane = new JScrollPane(new JList<>(allM));
        allPane.setBorder(BorderFactory.createTitledBorder("Requests (all)"));

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                tables,
                allPane);
        split.setResizeWeight(0.7);

        add(split, BorderLayout.CENTER);

        new Timer(500, e -> refresh()).start();
    }

    private void refresh() {
        bar.setMaximum(agg.producedTotal() == 0 ? 1 : (int) agg.producedTotal());
        bar.setValue((int) agg.consumedTotal());
        bar.setString(agg.consumedTotal() + " / " + agg.producedTotal());

        rebuild(clientM, agg.clientRows());
        rebuild(workerM, agg.workerRows());

        fill(allM, agg.allRequests());
    }

    private static void rebuild(DefaultTableModel m, List<Object[]> rows) {
        m.setRowCount(0);
        rows.forEach(m::addRow);
    }
    private static void fill(DefaultListModel<String> m, List<Request> rs) {
        m.clear();
        for (int i = rs.size()-1; i >= 0; i--) {
            Request r = rs.get(i);
            m.addElement("#" + r.getId() + " " + r.getType());
        }
    }
}