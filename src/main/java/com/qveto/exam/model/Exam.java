package com.qveto.exam.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Exam(
    Long id,
    String title,
    String description,
    int durationMinutes,
    int totalQuestions,
    BigDecimal totalMarks,
    boolean published,
    Long createdBy,
    LocalDateTime createdAt
) {

}
