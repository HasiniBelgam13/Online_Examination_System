package com.qveto.exam.model;

public enum AttemptStatus {
    IN_PROGRESS,
    SUBMITTED_MANUAL,
    SUBMITTED_AUTO,
    ABANDONED;

    public boolean isTerminal() {return this != IN_PROGRESS;}
}
