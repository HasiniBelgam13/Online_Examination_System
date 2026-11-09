package com.onlineexam.ui;

/**
 * UI boundary for authentication.
 * The backend/database team can implement this interface using JDBC and its AuthService.
 */
public interface AuthProvider {
    UserSession authenticate(String email, char[] password);
}
