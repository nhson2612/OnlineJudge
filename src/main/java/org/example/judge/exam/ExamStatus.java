package org.example.judge.exam;

public enum ExamStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    public boolean isActive() {
        return this == IN_PROGRESS;
    }

    public boolean isFinal() {
        return this == COMPLETED || this == CANCELLED;
    }
}
