package com.onlineexam.ui;

public final class UserSession {
    private final int userId;
    private final String fullName;
    private final String email;
    private final UserRole role;

    public UserSession(int userId, String fullName, String email, UserRole role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public UserRole getRole() { return role; }
}
