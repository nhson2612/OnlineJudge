package org.example.judge.core.domain;

public class OutputContext {
    private String actualOutputPath;
    private String expectedOutputPath;
    private long executionTime;
    private long memoryUsage;
    private int exitCode;

    public OutputContext(String actualOutputPath, String expectedOutputPath, long executionTime, long memoryUsage, int exitCode) {
        this.actualOutputPath = actualOutputPath;
        this.expectedOutputPath = expectedOutputPath;
        this.executionTime = executionTime;
        this.memoryUsage = memoryUsage;
        this.exitCode = exitCode;
    }

    public String getActualOutputPath() {
        return actualOutputPath;
    }

    public String getExpectedOutputPath() {
        return expectedOutputPath;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public long getMemoryUsage() {
        return memoryUsage;
    }

    public int getExitCode() {
        return exitCode;
    }
}
