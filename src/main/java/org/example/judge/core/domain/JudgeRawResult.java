package org.example.judge.core.domain;

public class JudgeRawResult {
    private String outputPath;
    private long executionTime;
    private long memoryUsage;
    private int exitCode;

    public JudgeRawResult(String outputPath, long executionTime, long memoryUsage, int exitCode) {
        this.outputPath = outputPath;
        this.executionTime = executionTime;
        this.memoryUsage = memoryUsage;
        this.exitCode = exitCode;
    }

    public String getOutputPath() {
        return outputPath;
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
