package org.example.judge.submission.model.dto;

public record TestCaseResultReq(String actualOutputPath,
                                String expectedOutputPath,
                                long executionTime,
                                long memoryUsage) {

    public TestCaseResultReq {
        if (actualOutputPath == null || expectedOutputPath == null) {
            throw new IllegalArgumentException("Paths cannot be null");
        }
        if (executionTime < 0 || memoryUsage < 0) {
            throw new IllegalArgumentException("Execution time and memory usage must be non-negative");
        }
    }
}
