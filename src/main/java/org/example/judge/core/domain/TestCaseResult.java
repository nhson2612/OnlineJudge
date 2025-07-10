package org.example.judge.core.domain;

public class TestCaseResult {
    private TestcaseResultType type;
    private String actualOutputPath;
    private String expectedOutputPath;
    private long executionTime;
    private long memoryUsage;

    public TestCaseResult() {}

    public TestCaseResult(TestcaseResultType type, String actualOutputPath, String expectedOutputPath, long executionTime, long memoryUsage) {
        this.type = type;
        this.actualOutputPath = actualOutputPath;
        this.expectedOutputPath = expectedOutputPath;
        this.executionTime = executionTime;
        this.memoryUsage = memoryUsage;
    }

    public TestcaseResultType getType() {
        return type;
    }

    public void setType(TestcaseResultType type) {
        this.type = type;
    }

    public String getActualOutputPath() {
        return actualOutputPath;
    }

    public void setActualOutputPath(String actualOutputPath) {
        this.actualOutputPath = actualOutputPath;
    }

    public String getExpectedOutputPath() {
        return expectedOutputPath;
    }

    public void setExpectedOutputPath(String expectedOutputPath) {
        this.expectedOutputPath = expectedOutputPath;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public long getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

}
