
package com.example.dao;

import com.example.model.Question;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

    // ADD QUESTION
    public boolean addQuestion(Question question) {

        String sql = "INSERT INTO questions "
                + "(exam_id, question_text, option_a, option_b, "
                + "option_c, option_d, correct_option) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, question.getExamId());
            ps.setString(2, question.getQuestionText());
            ps.setString(3, question.getOptionA());
            ps.setString(4, question.getOptionB());
            ps.setString(5, question.getOptionC());
            ps.setString(6, question.getOptionD());
            ps.setString(7, String.valueOf(
                    Character.toUpperCase(question.getCorrectOption())
            ));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Adding question failed: " + e.getMessage());
            return false;
        }
    }

    // GET ALL QUESTIONS FOR AN EXAM
    public List<Question> getQuestionsByExamId(int examId) {

        List<Question> questions = new ArrayList<>();

        String sql = "SELECT * FROM questions "
                + "WHERE exam_id = ? ORDER BY question_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, examId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    questions.add(mapQuestion(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Fetching questions failed: " + e.getMessage());
        }

        return questions;
    }

    // GET ONE QUESTION
    public Question getQuestionById(int questionId) {

        String sql = "SELECT * FROM questions WHERE question_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, questionId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapQuestion(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Fetching question failed: " + e.getMessage());
        }

        return null;
    }

    // UPDATE QUESTION
    public boolean updateQuestion(Question question) {

        String sql = "UPDATE questions SET "
                + "question_text = ?, "
                + "option_a = ?, "
                + "option_b = ?, "
                + "option_c = ?, "
                + "option_d = ?, "
                + "correct_option = ? "
                + "WHERE question_id = ? AND exam_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, question.getQuestionText());
            ps.setString(2, question.getOptionA());
            ps.setString(3, question.getOptionB());
            ps.setString(4, question.getOptionC());
            ps.setString(5, question.getOptionD());
            ps.setString(6, String.valueOf(
                    Character.toUpperCase(question.getCorrectOption())
            ));
            ps.setInt(7, question.getQuestionId());
            ps.setInt(8, question.getExamId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Updating question failed: " + e.getMessage());
            return false;
        }
    }

    // DELETE QUESTION
    public boolean deleteQuestion(int questionId) {

        String sql = "DELETE FROM questions WHERE question_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, questionId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Deleting question failed: " + e.getMessage()
            );
            return false;
        }
    }

    // CONVERT DATABASE ROW INTO QUESTION OBJECT
    private Question mapQuestion(ResultSet rs) throws SQLException {

        Question question = new Question();

        question.setQuestionId(rs.getInt("question_id"));
        question.setExamId(rs.getInt("exam_id"));
        question.setQuestionText(rs.getString("question_text"));
        question.setOptionA(rs.getString("option_a"));
        question.setOptionB(rs.getString("option_b"));
        question.setOptionC(rs.getString("option_c"));
        question.setOptionD(rs.getString("option_d"));

        String correct = rs.getString("correct_option");

        if (correct != null && !correct.isEmpty()) {
            question.setCorrectOption(
                    Character.toUpperCase(correct.charAt(0))
            );
        }

        return question;
    }
}
