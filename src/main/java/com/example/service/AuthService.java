
package com.example.service;

import com.example.dao.UserDAO;
import com.example.model.User;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    // Student registration
    public String registerStudent(String name, String email,
                                  String password) {

        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be empty!";
        }

        if (email == null || !email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return "Enter a valid email address!";
        }

        if (password == null || password.length() < 6) {
            return "Password must contain at least 6 characters!";
        }

        if (userDAO.emailExists(email.trim())) {
            return "Email already registered!";
        }

        User user = new User(
                name.trim(),
                email.trim(),
                password,
                "STUDENT"
        );

        boolean registered = userDAO.registerUser(user);

        return registered
                ? "Registration successful!"
                : "Registration failed!";
    }

    // User login
    public User login(String email, String password) {

        if (email == null || email.trim().isEmpty()
                || password == null || password.isEmpty()) {
            return null;
        }

        User user = userDAO.login(email.trim(), password);

        if (user == null) {
            return null;
        }

        return user;
    }

    // Admin login
    public User adminLogin(String email, String password) {

        User user = login(email, password);

        if (user != null && "ADMIN".equalsIgnoreCase(user.getRole())) {
            return user;
        }

        return null;
    }

    // Student login
    public User studentLogin(String email, String password) {

        User user = login(email, password);

        if (user != null && "STUDENT".equalsIgnoreCase(user.getRole())) {
            return user;
        }

        return null;
    }
}
