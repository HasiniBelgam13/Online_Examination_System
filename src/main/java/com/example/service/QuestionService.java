
package com.example.service;

import com.example.dao.QuestionDAO;
import com.example.model.Question;

import java.util.List;

public class QuestionService {

    private final QuestionDAO questionDAO;

    public QuestionService() {
        this.questionDAO = new QuestionDAO();
    }

    // ADD QUESTION
    public boolean addQuestion(Question question) {

        if (!isValidQuestion(question)) {
            return false;
        }

        question.setCorrectOption(
                Character.toUpperCase(question.getCorrectOption())
        );

        return questionDAO.addQuestion(question);
    }

    // GET QUESTIONS FOR AN EXAM
    public List<Question> getQuestionsByExamId(int examId) {

        if (examId <= 0) {
            return List.of();
        }

        return questionDAO.getQuestionsByExamId(examId);
    }

    // GET QUESTION BY ID
    public Question getQuestionById(int questionId) {

        if (questionId <= 0) {
            return null;
        }

        return questionDAO.getQuestionById(questionId);
    }

    // UPDATE QUESTION
    public boolean updateQuestion(Question question) {

        if (question == null || question.getQuestionId() <= 0) {
            return false;
        }

        if (!isValidQuestion(question)) {
            return false;
        }

        question.setCorrectOption(
                Character.toUpperCase(question.getCorrectOption())
        );

        return questionDAO.updateQuestion(question);
    }

    // DELETE QUESTION
    public boolean deleteQuestion(int questionId) {

        if (questionId <= 0) {
            return false;
        }

        return questionDAO.deleteQuestion(questionId);
    }

    // VALIDATE QUESTION
    private boolean isValidQuestion(Question question) {

        if (question == null || question.getExamId() <= 0) {
            return false;
        }

        if (isEmpty(question.getQuestionText())
                || isEmpty(question.getOptionA())
                || isEmpty(question.getOptionB())
                || isEmpty(question.getOptionC())
                || isEmpty(question.getOptionD())) {

            return false;
        }

        char correct = Character.toUpperCase(
                question.getCorrectOption()
        );

        return "ABCD".indexOf(correct) >= 0;
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
