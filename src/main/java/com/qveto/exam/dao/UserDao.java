package com.qveto.exam.dao;

import java.util.Optional;

import com.qveto.exam.model.User;

public interface UserDao {
    Optional<User> findByUserName(String userName);
    boolean updateProfile(Long userId, String newDisplayName, String newPasswordHash);
}
