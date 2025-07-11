package org.example.judge.submission.model.old;

import org.example.judge.core.domain.SubmissionStatus;

public class JudgeResult {
    private final String submissionId;
    private final SubmissionStatus status;
    private final String output;
    private final long executionTime;
    private final String errorMessage;
    private final int exitCode;

    public JudgeResult(String submissionId, SubmissionStatus status, String output,
                       long executionTime, String errorMessage, int exitCode) {
        this.submissionId = submissionId;
        this.status = status;
        this.output = output;
        this.executionTime = executionTime;
        this.errorMessage = errorMessage;
        this.exitCode = exitCode;
    }
    public String getSubmissionId() { return submissionId; }
    public SubmissionStatus getStatus() { return status; }
    public String getOutput() { return output; }
    public long getExecutionTime() { return executionTime; }
    public String getErrorMessage() { return errorMessage; }
    public int getExitCode() { return exitCode; }

    @Override
    public String toString() {
        return String.format("JudgeResult{id='%s', status=%s, time=%dms, output='%s', error='%s'}",
                submissionId, status, executionTime, output, errorMessage);
    }
}
