package com.onlineexam.ui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Use Swing's default look and feel if the system one is unavailable.
            }

            AppFrame app = new AppFrame(new DemoAuthProvider());
            app.setVisible(true);
        });
    }
}
