
package com.example.dao;

import com.example.model.Exam;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExamDAO {

    // Add a new exam
    public boolean addExam(Exam exam) {

        String sql = "INSERT INTO exams "
                   + "(title, duration_minutes, total_marks) "
                   + "VALUES (?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, exam.getTitle());
            ps.setInt(2, exam.getDurationMinutes());
            ps.setInt(3, exam.getTotalMarks());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Adding exam failed: " + e.getMessage());
            return false;
        }
    }

    // Get all exams
    public List<Exam> getAllExams() {

        List<Exam> exams = new ArrayList<>();

        String sql = "SELECT * FROM exams ORDER BY exam_id DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Exam exam = new Exam();

                exam.setExamId(rs.getInt("exam_id"));
                exam.setTitle(rs.getString("title"));
                exam.setDurationMinutes(rs.getInt("duration_minutes"));
                exam.setTotalMarks(rs.getInt("total_marks"));
                exam.setCreatedAt(rs.getTimestamp("created_at"));

                exams.add(exam);
            }

        } catch (SQLException e) {
            System.err.println("Fetching exams failed: " + e.getMessage());
        }

        return exams;
    }

    // Get an exam using its ID
    public Exam getExamById(int examId) {

        String sql = "SELECT * FROM exams WHERE exam_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, examId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Exam exam = new Exam();

                    exam.setExamId(rs.getInt("exam_id"));
                    exam.setTitle(rs.getString("title"));
                    exam.setDurationMinutes(rs.getInt("duration_minutes"));
                    exam.setTotalMarks(rs.getInt("total_marks"));
                    exam.setCreatedAt(rs.getTimestamp("created_at"));

                    return exam;
                }
            }

        } catch (SQLException e) {
            System.err.println("Fetching exam failed: " + e.getMessage());
        }

        return null;
    }

    // Delete an exam
    public boolean deleteExam(int examId) {

        String sql = "DELETE FROM exams WHERE exam_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, examId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Deleting exam failed: " + e.getMessage());
            return false;
        }
    }
}
