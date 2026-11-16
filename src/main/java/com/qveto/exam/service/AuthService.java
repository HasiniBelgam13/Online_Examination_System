package com.qveto.exam.service;

import java.util.Objects;
import com.qveto.exam.dao.UserDao;
import com.qveto.exam.model.User;
import com.qveto.exam.util.PasswordUtil;

public class AuthService {
    private final UserDao userDao;

    public AuthService(UserDao userDao) {
        this.userDao = Objects.requireNonNull(userDao, "userDao must not be null");
    }

    public User authenticate(String userName, char[] rawPassword) {
        if (PasswordUtil.isBlank(userName) || PasswordUtil.isBlank(rawPassword)) {
            if (rawPassword != null) {
                java.util.Arrays.fill(rawPassword, '\0');
            }
            throw new IllegalArgumentException("Username and password are required.");
        }

        User user = userDao.findByUserName(userName.trim())
            .orElseThrow(() -> {
                java.util.Arrays.fill(rawPassword, '\0');
                return new SecurityException("Invalid username or password.");
            });

        boolean passwordValid = PasswordUtil.verifyPassword(rawPassword, user.passwordHash());
        if (!passwordValid) {
            throw new SecurityException("Invalid username or password.");
        }

        if (!user.active()) {
            throw new SecurityException("Account is deactivated. Please contact the administrator.");
        }

        return user;
    }
}