package com.qveto.exam.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import javax.sql.DataSource;

import com.qveto.exam.dao.UserDao;
import com.qveto.exam.model.Role;
import com.qveto.exam.model.User;

public class UserDaoImpl implements UserDao {
    private final DataSource dataSource;

    public UserDaoImpl(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "dataSource must not be null");
    }

    @Override 
    public Optional<User> findByUserName(String userName) {
        String sql = """
            SELECT id, username, password_hash, display_name, role, is_active, created_at
            FROM users WHERE username = ?
        """;
        try(
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, userName);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return Optional.of(new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("display_name"),
                        Role.valueOf(rs.getString("role")),
                        rs.getBoolean("is_active"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                    ));
                }
                return Optional.empty();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Database error while fetching user: " + userName, e);
        }
    }

    @Override 
    public boolean updateProfile(
        Long userId,
        String newDisplayName,
        String newPasswordHash
    ) {
        String sql = """
            UPDATE users
            SET display_name = ?, password_hash = ?
            WHERE id = ?
        """;
        try(
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, newDisplayName);
            ps.setString(2, newPasswordHash);
            ps.setLong(3, userId);
            return ps.executeUpdate() == 1;
        } catch(SQLException e) {
            throw new RuntimeException("Database error updating profile for user ID: " + userId, e);
        }
    }
}
