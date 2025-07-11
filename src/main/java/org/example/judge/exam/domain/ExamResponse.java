package org.example.judge.exam.domain;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface ExamResponse {
    Long getId();
    String getName();
    Instant getStartTime();
    Instant getEndTime();
    ExamStatus getStatus();
    Instant getCreatedAt();
    Instant getUpdatedAt();
    Set<ClassRoomResponse> getClassRooms();
    Set<ProblemResponse> getProblems();
    interface ClassRoomResponse {
        Long getId();
        String getName();
    }
    interface ProblemResponse {
        Long getId();
        String getName();
        String getDescription();
        String getInputFormat();
        String getOutputFormat();
        int getTimeLimit();
        int getMemoryLimit();
        List<TestCaseResponse> getPublicTestCases();
        Instant getCreatedAt();
        String getInputFileName();
        String getExpectedOutputFileName();
    }
    interface TestCaseResponse {
        Long getOrderIndex();
    }
}
