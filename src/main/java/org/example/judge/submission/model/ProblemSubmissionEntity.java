package org.example.judge.submission.model;

import jakarta.persistence.*;
import org.example.judge.core.domain.Testcase;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "problem_submissions")
public class ProblemSubmissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sourceCodePath;
    private String language;
    private String score;
    private Long problemId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private SubmissionEntity submission;
    @OneToMany(mappedBy = "problemSubmission", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TestCaseResultEntity> results = new ArrayList<>();
    @Transient
    private List<Testcase> testcaseSet;

    public ProblemSubmissionEntity() {}
    public ProblemSubmissionEntity(Long problemId, String sourceCodePath, String language, List<TestCaseResultEntity> results, String score, List<Testcase> testcaseSet) {
        this.id = problemId;
        this.sourceCodePath = sourceCodePath;
        this.language = language;
        this.results = results;
        this.score = score;
        this.testcaseSet = testcaseSet;
    }

    public Long getId() {return id;}
    public void setId(Long id) {
        this.id = id;
    }
    public String getSourceCodePath() {
        return sourceCodePath;
    }
    public void setSourceCodePath(String sourceCodePath) {
        this.sourceCodePath = sourceCodePath;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public List<TestCaseResultEntity> getResults() {
        return results;
    }
    public void setResults(List<TestCaseResultEntity> results) {
        this.results = results;
    }
    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    public List<Testcase> getTestcaseSet() {
        return testcaseSet;
    }
    public void setTestcaseSet(List<Testcase> testcaseSet) {
        this.testcaseSet = testcaseSet;
    }
    public void setProblemId(Long problemId) {this.problemId=problemId;}
    public Long getProblemId() {return this.problemId;}
}