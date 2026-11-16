
package com.example.ui;

import com.example.model.User;
import com.example.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final AuthService authService = new AuthService();

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame() {

        setTitle("Online Examination System - Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createLoginUI();

        setVisible(true);
    }

    private void createLoginUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.setBackground(Color.WHITE);

        // HEADER
        JPanel header = new JPanel();

        header.setBackground(new Color(35, 55, 90));

        JLabel title = new JLabel("ONLINE EXAMINATION SYSTEM");

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font("Arial", Font.BOLD, 20)
        );

        header.add(title);

        mainPanel.add(header, BorderLayout.NORTH);

        // LOGIN FORM
        JPanel formPanel = new JPanel(
                new GridBagLayout()
        );

        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginTitle = new JLabel("User Login");

        loginTitle.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        loginTitle.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        formPanel.add(loginTitle, gbc);

        // EMAIL
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;

        formPanel.add(new JLabel("Email:"), gbc);

        emailField = new JTextField(20);

        gbc.gridx = 1;

        formPanel.add(emailField, gbc);

        // PASSWORD
        gbc.gridx = 0;
        gbc.gridy = 2;

        formPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);

        gbc.gridx = 1;

        formPanel.add(passwordField, gbc);

        // LOGIN BUTTON
        JButton loginButton = new JButton("Login");

        loginButton.setBackground(
                new Color(35, 55, 90)
        );

        loginButton.setForeground(Color.WHITE);

        loginButton.addActionListener(
                e -> loginUser()
        );

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        formPanel.add(loginButton, gbc);

        // REGISTER BUTTON
        JButton registerButton =
                new JButton("Register as Student");

        registerButton.addActionListener(
                e -> registerStudent()
        );

        gbc.gridy = 4;

        formPanel.add(registerButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // LOGIN USER
    private void loginUser() {

        String email = emailField.getText().trim();

        String password =
                new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter email and password."
            );

            return;
        }

        User user = authService.login(email, password);

        if (user == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid email or password."
            );

            return;
        }

        String role = user.getRole();

        if ("ADMIN".equalsIgnoreCase(role)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Welcome Administrator!"
            );

            dispose();

            new AdminDashboard();

        } else if ("STUDENT".equalsIgnoreCase(role)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Welcome " + user.getName() + "!"
            );

            dispose();

            new StudentDashboard(user);

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid user role."
            );
        }
    }

    // STUDENT REGISTRATION
    private void registerStudent() {

        JTextField nameField = new JTextField(20);

        JTextField emailInput = new JTextField(20);

        JPasswordField passwordInput =
                new JPasswordField(20);

        Object[] fields = {
                "Full Name:", nameField,
                "Email:", emailInput,
                "Password:", passwordInput
        };

        int choice = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Student Registration",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        String name = nameField.getText().trim();

        String email = emailInput.getText().trim();

        String password =
                new String(passwordInput.getPassword());

        String message = authService.registerStudent(
                name,
                email,
                password
        );

        JOptionPane.showMessageDialog(
                this,
                message
        );
    }
}
