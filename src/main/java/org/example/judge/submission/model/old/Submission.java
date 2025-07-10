package org.example.judge.submission.model.old;

import org.example.judge.submission.model.SubmissionStatus;

public class Submission {
    private final String id;
    private final String sourceCodePath;
    private final String inputDataPath;
    private final String language;
    private volatile SubmissionStatus status;
    private volatile JudgeResult result;

    public Submission(String id, String sourceCodePath, String inputDataPath, String language) {
        this.id = id;
        this.sourceCodePath = sourceCodePath;
        this.inputDataPath = inputDataPath;
        this.language = language;
        this.status = SubmissionStatus.PENDING;
    }
    public String getId() { return id; }
    public String getSourceCodePath() { return sourceCodePath; }
    public String getInputDataPath() { return inputDataPath; }
    public String getLanguage() { return language; }
    public SubmissionStatus getStatus() { return status; }
    public void setStatus(SubmissionStatus status) { this.status = status; }
    public JudgeResult getResult() { return result; }
    public void setResult(JudgeResult result) { this.result = result; }
}
