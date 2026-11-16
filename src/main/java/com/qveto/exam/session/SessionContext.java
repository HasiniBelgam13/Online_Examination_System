package com.qveto.exam.session;

import com.qveto.exam.model.Role;
import com.qveto.exam.model.User;

public final class SessionContext {
    private User currentUser;
    private Long activeAttemptId;

    public synchronized void login(User user) {
        this.currentUser = user;
        this.activeAttemptId = null;
    }

    public synchronized void logout() {
        this.currentUser = null;
        this.activeAttemptId = null;
    }

    public synchronized User getCurrentUser() {
        if(currentUser == null) {
            throw new SecurityException("Authentication required.");
        }
        return currentUser;
    }

    public synchronized boolean isAuthenticated() {
        return currentUser != null;
    }

    public synchronized void requireRole(Role expectedRole) {
        User user = getCurrentUser();
        if(user.role() != expectedRole) {
            throw new SecurityException("Access denied: requires role " + expectedRole);
        }
    }

    public synchronized void setActiveAttemptId(Long attemptId) {
        this.activeAttemptId = attemptId;
    }

    public synchronized Long getActiveAttemptId() {
        return activeAttemptId;
    }
}
