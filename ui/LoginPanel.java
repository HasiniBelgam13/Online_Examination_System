package com.onlineexam.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.BiFunction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginPanel extends JPanel {
    private final JTextField emailField = new JTextField(24);
    private final JPasswordField passwordField = new JPasswordField(24);
    private final JLabel messageLabel = new JLabel(" ", SwingConstants.CENTER);
    private final BiFunction<String, char[], UserSession> loginAction;

    public LoginPanel(BiFunction<String, char[], UserSession> loginAction) {
        this.loginAction = loginAction;
        setLayout(new GridBagLayout());
        setBackground(Theme.BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(225, 230, 238)),
                BorderFactory.createEmptyBorder(32, 38, 32, 38)));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(7, 8, 7, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("ONLINE EXAMINATION SYSTEM", SwingConstants.CENTER);
        title.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 22));
        title.setForeground(Theme.NAVY);
        card.add(title, c);

        c.gridy++;
        JLabel subtitle = new JLabel("Sign in to continue", SwingConstants.CENTER);
        subtitle.setForeground(Theme.MUTED);
        card.add(subtitle, c);

        c.gridwidth = 1;
        c.gridy++;
        c.gridx = 0;
        card.add(new JLabel("Email address"), c);
        c.gridx = 1;
        card.add(emailField, c);

        c.gridy++;
        c.gridx = 0;
        card.add(new JLabel("Password"), c);
        c.gridx = 1;
        card.add(passwordField, c);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        messageLabel.setForeground(new java.awt.Color(190, 45, 45));
        card.add(messageLabel, c);

        c.gridy++;
        JButton loginButton = Theme.primaryButton("Login");
        card.add(loginButton, c);

        c.gridy++;
        JLabel note = new JLabel(
                "<html><center>Use your account credentials.<br>"
                + "Demo: admin@exam.com / Admin@123<br>"
                + "Demo: student@exam.com / Student@123</center></html>",
                SwingConstants.CENTER);
        note.setForeground(Theme.MUTED);
        note.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 11));
        card.add(note, c);

        loginButton.addActionListener(e -> attemptLogin());
        passwordField.addActionListener(e -> attemptLogin());

        add(card, new GridBagConstraints());
    }

    private void attemptLogin() {
        String email = emailField.getText().trim();
        char[] password = passwordField.getPassword();

        if (email.isEmpty() || password.length == 0) {
            messageLabel.setText("Please enter both email and password.");
            java.util.Arrays.fill(password, '\0');
            return;
        }

        UserSession session = loginAction.apply(email, password);
        java.util.Arrays.fill(password, '\0');

        if (session == null) {
            messageLabel.setText("Invalid email or password. Please try again.");
        } else {
            messageLabel.setText(" ");
            passwordField.setText("");
        }
    }

    public void showError(String message) {
        messageLabel.setText(message);
    }
}
