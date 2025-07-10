package org.example.judge.submission.model.old;

public class JudgeRawResult {
    private final int exitCode;
    private final String stdout;
    private final String stderr;
    private final long executionTime;

    public JudgeRawResult(int exitCode, String stdout, String stderr, long executionTime) {
        this.exitCode = exitCode;
        this.stdout = stdout;
        this.stderr = stderr;
        this.executionTime = executionTime;
    }

    public int getExitCode() { return exitCode; }
    public String getStdout() { return stdout; }
    public String getStderr() { return stderr; }
    public long getExecutionTime() { return executionTime; }
}
