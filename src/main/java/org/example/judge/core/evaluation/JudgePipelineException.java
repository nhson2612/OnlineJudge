package org.example.judge.core.evaluation;

public class JudgePipelineException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final int exitCode;

    public JudgePipelineException(String message, int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }
    public int getExitCode() {
        return exitCode;
    }
}
