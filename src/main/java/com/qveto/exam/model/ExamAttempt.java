package com.qveto.exam.model;

import java.time.LocalDateTime;

public record ExamAttempt(
    Long id,
    Long examId,
    Long studentId,
    AttemptStatus status,
    LocalDateTime startedAt,
    LocalDateTime expiresAt,
    LocalDateTime submittedAt
) {
    public boolean isExpired(LocalDateTime now) {
        return !now.isBefore(expiresAt);
    }
}
