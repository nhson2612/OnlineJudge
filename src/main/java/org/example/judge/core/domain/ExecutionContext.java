package org.example.judge.core.domain;

public class ExecutionContext {
    private Testcase testcase;
    private String sourceCodePath;

    public ExecutionContext(Testcase testcase, String sourceCodePath) {
        this.testcase = testcase;
        this.sourceCodePath = sourceCodePath;
    }

    public Testcase getTestcase() {
        return testcase;
    }

    public String getSourceCodePath() {
        return sourceCodePath;
    }
}
