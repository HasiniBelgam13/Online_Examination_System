
package com.example.dao;

import com.example.model.StudentAnswer;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentAnswerDAO {

    // Save a new answer or update an existing answer
    public boolean saveOrUpdateAnswer(StudentAnswer answer) {

        String checkSql = "SELECT answer_id FROM student_answers "
                        + "WHERE user_id = ? AND exam_id = ? "
                        + "AND question_id = ?";

        try (Connection con = DatabaseConnection.getConnection()) {

            try (PreparedStatement check = con.prepareStatement(checkSql)) {

                check.setInt(1, answer.getUserId());
                check.setInt(2, answer.getExamId());
                check.setInt(3, answer.getQuestionId());

                try (ResultSet rs = check.executeQuery()) {

                    if (rs.next()) {
                        return updateAnswer(con, answer);
                    }
                }
            }

            return insertAnswer(con, answer);

        } catch (SQLException e) {
            System.err.println("Saving answer failed: " + e.getMessage());
            return false;
        }
    }

    // Insert a new answer
    private boolean insertAnswer(Connection con, StudentAnswer answer)
            throws SQLException {

        String sql = "INSERT INTO student_answers "
                   + "(user_id, exam_id, question_id, selected_option) "
                   + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, answer.getUserId());
            ps.setInt(2, answer.getExamId());
            ps.setInt(3, answer.getQuestionId());
            ps.setString(4, String.valueOf(answer.getSelectedOption()));

            return ps.executeUpdate() > 0;
        }
    }

    // Update an existing answer
    private boolean updateAnswer(Connection con, StudentAnswer answer)
            throws SQLException {

        String sql = "UPDATE student_answers SET selected_option = ? "
                   + "WHERE user_id = ? AND exam_id = ? AND question_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, String.valueOf(answer.getSelectedOption()));
            ps.setInt(2, answer.getUserId());
            ps.setInt(3, answer.getExamId());
            ps.setInt(4, answer.getQuestionId());

            return ps.executeUpdate() > 0;
        }
    }

    // Retrieve all saved answers for a student and exam
    public List<StudentAnswer> getAnswers(int userId, int examId) {

        List<StudentAnswer> answers = new ArrayList<>();

        String sql = "SELECT * FROM student_answers "
                   + "WHERE user_id = ? AND exam_id = ? "
                   + "ORDER BY question_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, examId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    StudentAnswer answer = new StudentAnswer();

                    answer.setAnswerId(rs.getInt("answer_id"));
                    answer.setUserId(rs.getInt("user_id"));
                    answer.setExamId(rs.getInt("exam_id"));
                    answer.setQuestionId(rs.getInt("question_id"));

                    String selected = rs.getString("selected_option");
                    answer.setSelectedOption(selected.charAt(0));

                    answer.setAnsweredAt(rs.getTimestamp("answered_at"));

                    answers.add(answer);
                }
            }

        } catch (SQLException e) {
            System.err.println("Fetching answers failed: " + e.getMessage());
        }

        return answers;
    }
}
