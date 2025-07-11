package org.example.judge.exam.domain;

import java.time.Instant;

public interface StudentExamView {
    Long getId();
    String getName();
    Instant getStartTime();
    Instant getEndTime();
    ExamStatus getStatus();
}
