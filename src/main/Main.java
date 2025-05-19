package main;

import gui.SystemMonitorGUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            int clients  = ask("Clients",          5);
            int workers  = ask("Workers",          3);
            int capacity = ask("Queue capacity", 128);
            int limit    = ask("Total requests", 512);

            new SystemMonitorGUI(clients, workers, capacity, limit)
                    .setVisible(true);
        });
    }

    private static int ask(String msg, int def) {
        String s = JOptionPane.showInputDialog(null, msg, def);
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}