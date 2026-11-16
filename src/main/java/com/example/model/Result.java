
package com.example.model;

import java.sql.Timestamp;

public class Result {

    private int resultId;
    private int userId;
    private int examId;
    private int score;
    private Timestamp submittedAt;

    public Result() {
    }

    public Result(int userId, int examId, int score) {
        this.userId = userId;
        this.examId = examId;
        this.score = score;
    }

    public Result(int resultId, int userId, int examId,
                  int score, Timestamp submittedAt) {
        this.resultId = resultId;
        this.userId = userId;
        this.examId = examId;
        this.score = score;
        this.submittedAt = submittedAt;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }
}
