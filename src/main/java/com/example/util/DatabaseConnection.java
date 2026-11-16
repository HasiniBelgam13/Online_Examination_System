
package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/online_examination";

    private static final String USER = "exam_app";

    // Read the password from an environment variable.
    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    public static Connection getConnection()
            throws SQLException {

        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                    "DB_PASSWORD environment variable is not configured."
            );
        }

        return DriverManager.getConnection(
                URL, USER, PASSWORD
        );
    }

    public static void main(String[] args) {

        try (Connection connection = getConnection()) {

            System.out.println(
                    "MySQL Connected Successfully!"
            );

            System.out.println(
                    "Database: " + connection.getCatalog()
            );

            System.out.println(
                    "Auto Commit: " + connection.getAutoCommit()
            );

            System.out.println(
                    "Connected User: "
                            + connection.getMetaData().getUserName()
            );

            System.out.println(
                    "Server: "
                            + connection.getMetaData().getURL()
            );

        } catch (SQLException e) {

            System.out.println(
                    "Database Connection Failed!"
            );

            e.printStackTrace();
        }
    }
}
