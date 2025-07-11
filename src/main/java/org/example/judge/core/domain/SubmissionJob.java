package org.example.judge.core.domain;

import java.util.List;

public class SubmissionJob {
    private String submissionId;
    private List<ProblemSubmission> problems;
    private SubmissionStatus overallStatus;
    private String overallScore;

    public SubmissionJob(String submissionId, List<ProblemSubmission> problems, SubmissionStatus overallStatus, String overallScore) {
        this.submissionId = submissionId;
        this.problems = problems;
        this.overallStatus = overallStatus;
        this.overallScore = overallScore;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public List<ProblemSubmission> getProblems() {
        return problems;
    }

    public SubmissionStatus getOverallStatus() {
        return overallStatus;
    }

    public String getOverallScore() {
        return overallScore;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public void setProblems(List<ProblemSubmission> problems) {
        this.problems = problems;
    }

    public void setOverallStatus(SubmissionStatus overallStatus) {
        this.overallStatus = overallStatus;
    }

    public void setOverallScore(long passedCount) {
        this.overallScore = passedCount + "/" + this.problems.size();
    }
}
