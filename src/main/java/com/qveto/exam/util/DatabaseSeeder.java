package com.qveto.exam.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import com.qveto.exam.config.DatabaseConfig;

/**
 * Run once after executing schema.sql and seed.sql to populate valid Argon2
 * hashes for the synthetic demo accounts.
 */
public final class DatabaseSeeder {

    private DatabaseSeeder() {}

    public static void main(String[] args) {
        Map<String, String> demoAccounts = Map.of(
            "admin01",   "Admin@123",
            "student01", "Student@123",
            "student02", "Student@123",
            "student03", "Student@123"
        );

        String updateSql = "UPDATE users SET password_hash = ? WHERE username = ?";

        try (
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(updateSql)
        ) {
            conn.setAutoCommit(false);
            for (Map.Entry<String, String> entry : demoAccounts.entrySet()) {
                String username = entry.getKey();
                char[] rawPassword = entry.getValue().toCharArray();
                String argon2Hash = PasswordUtil.hashPassword(rawPassword);

                ps.setString(1, argon2Hash);
                ps.setString(2, username);
                ps.addBatch();
            }
            int[] updated = ps.executeBatch();
            conn.commit();
            System.out.println("Successfully updated Argon2 hashes for " + updated.length + " demo users.");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to seed Argon2 user passwords", e);
        } finally {
            DatabaseConfig.shutdown();
        }
    }
}