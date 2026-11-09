package com.qveto.exam.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExamResult(
    Long id,
    Long attemptId,
    int totalQuestions,
    int attemptedQuestions,
    int correctAnswers,
    int incorrectAnswers,
    BigDecimal scoreObtained,
    BigDecimal totalMarks,
    BigDecimal percentage,
    LocalDateTime calculatedAt
) {

}
