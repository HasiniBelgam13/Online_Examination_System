
package com.example.service;

import com.example.dao.ExamDAO;
import com.example.model.Exam;

import java.util.List;

public class ExamService {

    private final ExamDAO examDAO;

    public ExamService() {
        this.examDAO = new ExamDAO();
    }

    // Create a new exam
    public boolean createExam(String title,
                              int durationMinutes,
                              int totalMarks) {

        if (title == null || title.trim().isEmpty()) {
            System.out.println("Exam title cannot be empty!");
            return false;
        }

        if (durationMinutes <= 0) {
            System.out.println("Duration must be greater than zero!");
            return false;
        }

        if (totalMarks <= 0) {
            System.out.println("Total marks must be greater than zero!");
            return false;
        }

        Exam exam = new Exam(
                title.trim(),
                durationMinutes,
                totalMarks
        );

        return examDAO.addExam(exam);
    }

    // Retrieve all exams
    public List<Exam> getAllExams() {
        return examDAO.getAllExams();
    }

    // Retrieve an exam by ID
    public Exam getExamById(int examId) {

        if (examId <= 0) {
            return null;
        }

        return examDAO.getExamById(examId);
    }

    // Delete an exam
    public boolean deleteExam(int examId) {

        if (examId <= 0) {
            return false;
        }

        return examDAO.deleteExam(examId);
    }
}
