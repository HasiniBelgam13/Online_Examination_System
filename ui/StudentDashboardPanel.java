package com.onlineexam.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StudentDashboardPanel extends JPanel {
    public StudentDashboardPanel(UserSession session, Runnable logoutAction) {
        setLayout(new BorderLayout(18, 18));
        setBackground(Theme.BG);
        setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Student Dashboard");
        title.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 27));
        title.setForeground(Theme.NAVY);
        header.add(title, BorderLayout.WEST);

        JButton logout = Theme.secondaryButton("Logout");
        logout.addActionListener(e -> logoutAction.run());
        header.add(logout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(10, 18));
        content.setOpaque(false);

        JLabel welcome = new JLabel(
                "<html><b>Hello, " + escape(session.getFullName())
                + "!</b><br><font color='#697386'>View available exams and track your progress.</font></html>");
        welcome.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 17));
        content.add(welcome, BorderLayout.NORTH);

        JPanel actions = new JPanel(new GridLayout(2, 2, 18, 18));
        actions.setOpaque(false);
        actions.add(actionCard("Available Exams", "View exams that are open for you."));
        actions.add(actionCard("Exam Instructions", "Read rules and duration before starting an exam."));
        actions.add(actionCard("My Results", "Review marks and results from submitted exams."));
        actions.add(actionCard("My Profile", "View or update your student profile."));
        content.add(actions, BorderLayout.CENTER);

        JLabel footer = new JLabel("Signed in as " + escape(session.getEmail()) + "  •  STUDENT",
                SwingConstants.RIGHT);
        footer.setForeground(Theme.MUTED);
        add(content, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel actionCard(String title, String description) {
        JPanel card = new JPanel(new BorderLayout(8, 12));
        card.setBackground(Theme.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(225, 230, 238)),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)));

        JLabel heading = new JLabel(title);
        heading.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18));
        heading.setForeground(Theme.NAVY);

        JLabel detail = new JLabel("<html><body style='width:220px'>"
                + description + "</body></html>");
        detail.setForeground(Theme.MUTED);

        JButton open = Theme.secondaryButton("Open");
        open.addActionListener(e -> javax.swing.JOptionPane.showMessageDialog(
                this, title + " screen will be connected by the relevant module.",
                "Module placeholder", javax.swing.JOptionPane.INFORMATION_MESSAGE));

        card.add(heading, BorderLayout.NORTH);
        card.add(detail, BorderLayout.CENTER);
        card.add(open, BorderLayout.SOUTH);
        return card;
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
