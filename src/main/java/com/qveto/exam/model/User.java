package com.qveto.exam.model;

import java.time.LocalDateTime;

public record User(
    Long id,
    String userName,
    String passwordHash,
    String displayName,
    Role role,
    boolean active,
    LocalDateTime createdAt
) {
    
}
