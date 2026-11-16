
package com.example.service;

import com.example.dao.StudentAnswerDAO;
import com.example.model.StudentAnswer;

import java.util.List;

public class StudentAnswerService {

    private final StudentAnswerDAO studentAnswerDAO;

    public StudentAnswerService() {
        this.studentAnswerDAO = new StudentAnswerDAO();
    }

    // Save or update a student's answer
    public boolean saveAnswer(StudentAnswer answer) {

        if (answer == null) {
            return false;
        }

        char option = Character.toUpperCase(
                answer.getSelectedOption()
        );

        if (option < 'A' || option > 'D') {
            return false;
        }

        answer.setSelectedOption(option);

        return studentAnswerDAO.saveOrUpdateAnswer(answer);
    }

    // Retrieve all answers for a student in an exam
    public List<StudentAnswer> getStudentAnswers(
            int userId, int examId) {

        if (userId <= 0 || examId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid student ID or exam ID"
            );
        }

        return studentAnswerDAO.getAnswers(userId, examId);
    }
}
