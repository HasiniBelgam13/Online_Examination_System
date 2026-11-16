
package com.example.model;

import java.sql.Timestamp;

public class Exam {

    private int examId;
    private String title;
    private int durationMinutes;
    private int totalMarks;
    private Timestamp createdAt;

    public Exam() {
    }

    public Exam(int examId, String title,
                int durationMinutes, int totalMarks) {
        this.examId = examId;
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.totalMarks = totalMarks;
    }

    public Exam(String title, int durationMinutes, int totalMarks) {
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.totalMarks = totalMarks;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return title;
    }
}
