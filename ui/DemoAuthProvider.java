package com.onlineexam.ui;

/**
 * Temporary in-memory login provider for UI demonstration only.
 * Replace this with the real JDBC-backed authentication implementation.
 */
public final class DemoAuthProvider implements AuthProvider {
    @Override
    public UserSession authenticate(String email, char[] password) {
        String entered = new String(password);

        if ("admin@exam.com".equalsIgnoreCase(email)
                && "Admin@123".equals(entered)) {
            return new UserSession(1, "System Administrator",
                    "admin@exam.com", UserRole.ADMIN);
        }

        if ("student@exam.com".equalsIgnoreCase(email)
                && "Student@123".equals(entered)) {
            return new UserSession(2, "Demo Student",
                    "student@exam.com", UserRole.STUDENT);
        }

        return null;
    }
}
