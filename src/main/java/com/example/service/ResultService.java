
package com.example.service;

import com.example.dao.QuestionDAO;
import com.example.dao.ResultDAO;
import com.example.dao.StudentAnswerDAO;
import com.example.model.Exam;
import com.example.model.Question;
import com.example.model.Result;
import com.example.model.StudentAnswer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultService {

    private final ResultDAO resultDAO = new ResultDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final StudentAnswerDAO answerDAO = new StudentAnswerDAO();
    private final ExamService examService = new ExamService();

    // Stores calculated examination details
    public static class ResultSummary {

        private final int totalQuestions;
        private final int correctAnswers;
        private final int incorrectAnswers;
        private final int score;
        private final int totalMarks;
        private final double percentage;

        public ResultSummary(
                int totalQuestions,
                int correctAnswers,
                int incorrectAnswers,
                int score,
                int totalMarks,
                double percentage) {

            this.totalQuestions = totalQuestions;
            this.correctAnswers = correctAnswers;
            this.incorrectAnswers = incorrectAnswers;
            this.score = score;
            this.totalMarks = totalMarks;
            this.percentage = percentage;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public int getCorrectAnswers() {
            return correctAnswers;
        }

        public int getIncorrectAnswers() {
            return incorrectAnswers;
        }

        public int getScore() {
            return score;
        }

        public int getTotalMarks() {
            return totalMarks;
        }

        public double getPercentage() {
            return percentage;
        }
    }

    // Calculate result details
    public ResultSummary getResultSummary(int userId, int examId) {

        if (userId <= 0 || examId <= 0) {
            return null;
        }

        Exam exam = examService.getExamById(examId);

        if (exam == null) {
            return null;
        }

        List<Question> questions =
                questionDAO.getQuestionsByExamId(examId);

        List<StudentAnswer> answers =
                answerDAO.getAnswers(userId, examId);

        if (questions == null || questions.isEmpty()) {
            return null;
        }

        Map<Integer, Character> answerMap = new HashMap<>();

        if (answers != null) {
            for (StudentAnswer answer : answers) {
                answerMap.put(
                        answer.getQuestionId(),
                        Character.toUpperCase(
                                answer.getSelectedOption()
                        )
                );
            }
        }

        int correctCount = 0;

        for (Question question : questions) {

            Character selected =
                    answerMap.get(question.getQuestionId());

            if (selected != null &&
                    selected == Character.toUpperCase(
                            question.getCorrectOption())) {

                correctCount++;
            }
        }

        int totalQuestions = questions.size();

        // Unanswered questions count as incorrect.
        int incorrectCount = totalQuestions - correctCount;

        int totalMarks = exam.getTotalMarks();

        int score = (int) Math.round(
                (double) correctCount * totalMarks / totalQuestions
        );

        double percentage =
                (double) correctCount * 100 / totalQuestions;

        return new ResultSummary(
                totalQuestions,
                correctCount,
                incorrectCount,
                score,
                totalMarks,
                percentage
        );
    }

    // Calculate and save examination result
    public boolean calculateAndSaveResult(int userId, int examId) {

        if (userId <= 0 || examId <= 0) {
            return false;
        }

        // Prevent duplicate submissions
        List<Result> existingResults =
                resultDAO.getResultsByUserId(userId);

        if (existingResults != null) {

            for (Result existing : existingResults) {

                if (existing.getExamId() == examId) {

                    System.out.println(
                            "Result already submitted for this exam."
                    );

                    return false;
                }
            }
        }

        ResultSummary summary =
                getResultSummary(userId, examId);

        if (summary == null) {
            return false;
        }

        Result result = new Result(
                userId,
                examId,
                summary.getScore()
        );

        return resultDAO.saveResult(result);
    }

    // Get student's results
    public List<Result> getStudentResults(int userId) {

        if (userId <= 0) {
            return List.of();
        }

        return resultDAO.getResultsByUserId(userId);
    }

    // Get all results for admin
    public List<Result> getAllResults() {
        return resultDAO.getAllResults();
    }
}
