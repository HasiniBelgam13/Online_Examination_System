
package com.example;

import com.example.dao.ExamDAO;
import com.example.model.Exam;

public class ExamTest {

    public static void main(String[] args) {

        ExamDAO dao = new ExamDAO();

        Exam exam = new Exam(
                "Java Programming Test",
                30,
                10
        );

        boolean result = dao.addExam(exam);

        System.out.println(
                "Exam insertion result: " + result
        );
    }
}
