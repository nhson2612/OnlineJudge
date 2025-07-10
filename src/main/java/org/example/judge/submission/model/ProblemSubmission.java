package org.example.judge.submission.model;

import org.example.judge.core.domain.TestCaseResult;
import org.example.judge.core.domain.Testcase;
import org.example.judge.core.domain.TestcaseResultType;

import java.util.List;

public class ProblemSubmission {
    private Long problemId;
    private String sourceCodePath;
    private String language;
    private List<TestCaseResult> result;
    private String score;
    private List<Testcase> testcaseSet;

    public ProblemSubmission(Long problemId, String sourceCodePath, String language, List<TestCaseResult> result, String score, List<String> testCasePaths) {
        this.problemId = problemId;
        this.sourceCodePath = sourceCodePath;
        this.language = language;
        this.result = result;
        this.score = score;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public void setSourceCodePath(String sourceCodePath) {
        this.sourceCodePath = sourceCodePath;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setResult(List<TestCaseResult> result) {
        this.result = result;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Long getProblemId() {
        return problemId;
    }

    public String getSourceCodePath() {
        return sourceCodePath;
    }

    public String getLanguage() {
        return language;
    }

    public List<TestCaseResult> getResult() {
        return result;
    }

    public String getScore() {
        return score;
    }

    public List<Testcase> getTestCases() {
        return testcaseSet;
    }

    public void setTestCases(List<Testcase> testCasePaths) {
        this.testcaseSet = testCasePaths;
    }

    public boolean isPassed() {
        return result.stream().allMatch(r -> r.getType() == TestcaseResultType.PASSED);
    }
}