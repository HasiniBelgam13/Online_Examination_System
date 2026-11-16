package com.onlineexam.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;

final class Theme {
    static final Color NAVY = new Color(25, 45, 85);
    static final Color BLUE = new Color(48, 104, 190);
    static final Color BG = new Color(245, 247, 251);
    static final Color TEXT = new Color(35, 43, 58);
    static final Color MUTED = new Color(105, 115, 130);
    static final Color WHITE = Color.WHITE;

    private Theme() {}

    static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(WHITE);
        button.setBackground(BLUE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(11, 18, 11, 18));
        return button;
    }

    static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setForeground(NAVY);
        button.setBackground(WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 222, 233)),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        return button;
    }
}
