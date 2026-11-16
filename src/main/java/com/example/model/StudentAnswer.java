
package com.example.model;

import java.sql.Timestamp;

public class StudentAnswer {

    private int answerId;
    private int userId;
    private int examId;
    private int questionId;
    private char selectedOption;
    private Timestamp answeredAt;

    public StudentAnswer() {
    }

    public StudentAnswer(int userId, int examId,
                         int questionId, char selectedOption) {
        this.userId = userId;
        this.examId = examId;
        this.questionId = questionId;
        this.selectedOption = Character.toUpperCase(selectedOption);
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public char getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(char selectedOption) {
        this.selectedOption = Character.toUpperCase(selectedOption);
    }

    public Timestamp getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(Timestamp answeredAt) {
        this.answeredAt = answeredAt;
    }
}
