package org.example.judge.core.domain;

public class Testcase {
    private String inputPath;
    private String expectedOutputPath;
    private long timeLimit;
    private long memoryLimit;
    private int orderIndex;

    public Testcase(String inputPath, String expectedOutputPath, long timeLimit, long memoryLimit, int orderIndex) {
        this.inputPath = inputPath;
        this.expectedOutputPath = expectedOutputPath;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.orderIndex = orderIndex;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getExpectedOutputPath() {
        return expectedOutputPath;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public long getMemoryLimit() {
        return memoryLimit;
    }
}
