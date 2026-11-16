
package com.example.dao;

import com.example.model.Result;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultDAO {

    // Save a student's exam result
    public boolean saveResult(Result result) {

        String sql = "INSERT INTO results (user_id, exam_id, score) "
                   + "VALUES (?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, result.getUserId());
            ps.setInt(2, result.getExamId());
            ps.setInt(3, result.getScore());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Saving result failed: " + e.getMessage());
            return false;
        }
    }

    // Get results of a particular student
    public List<Result> getResultsByUserId(int userId) {

        List<Result> results = new ArrayList<>();

        String sql = "SELECT * FROM results WHERE user_id = ? "
                   + "ORDER BY submitted_at DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Result result = new Result();

                    result.setResultId(rs.getInt("result_id"));
                    result.setUserId(rs.getInt("user_id"));
                    result.setExamId(rs.getInt("exam_id"));
                    result.setScore(rs.getInt("score"));
                    result.setSubmittedAt(rs.getTimestamp("submitted_at"));

                    results.add(result);
                }
            }

        } catch (SQLException e) {
            System.err.println("Fetching student results failed: "
                    + e.getMessage());
        }

        return results;
    }

    // Get all results for admin
    public List<Result> getAllResults() {

        List<Result> results = new ArrayList<>();

        String sql = "SELECT * FROM results ORDER BY submitted_at DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Result result = new Result();

                result.setResultId(rs.getInt("result_id"));
                result.setUserId(rs.getInt("user_id"));
                result.setExamId(rs.getInt("exam_id"));
                result.setScore(rs.getInt("score"));
                result.setSubmittedAt(rs.getTimestamp("submitted_at"));

                results.add(result);
            }

        } catch (SQLException e) {
            System.err.println("Fetching all results failed: "
                    + e.getMessage());
        }

        return results;
    }
}
