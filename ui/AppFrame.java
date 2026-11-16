package com.onlineexam.ui;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AppFrame extends JFrame {
    private static final String LOGIN = "LOGIN";
    private static final String ADMIN = "ADMIN";
    private static final String STUDENT = "STUDENT";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final AuthProvider authProvider;
    private UserSession currentSession;

    public AppFrame(AuthProvider authProvider) {
        this.authProvider = authProvider;

        setTitle("Online Examination System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setSize(1050, 680);
        setLocationRelativeTo(null);

        cards.add(new LoginPanel(this::handleLogin), LOGIN);
        add(cards);
        cardLayout.show(cards, LOGIN);
    }

    private UserSession handleLogin(String email, char[] password) {
        UserSession session = authProvider.authenticate(email, password);

        if (session == null) {
            return null;
        }

        currentSession = session;
        if (session.getRole() == UserRole.ADMIN) {
            cards.add(new AdminDashboardPanel(session, this::logout), ADMIN);
            cardLayout.show(cards, ADMIN);
        } else if (session.getRole() == UserRole.STUDENT) {
            cards.add(new StudentDashboardPanel(session, this::logout), STUDENT);
            cardLayout.show(cards, STUDENT);
        }
        revalidate();
        repaint();
        return session;
    }

    private void logout() {
        currentSession = null;
        cardLayout.show(cards, LOGIN);
    }

    public UserSession getCurrentSession() {
        return currentSession;
    }
}
