package org.example.judge.submission.model;

import jakarta.persistence.*;
import org.example.judge.core.domain.SubmissionStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "submissions")
public class SubmissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long submissionId;
    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProblemSubmissionEntity> problems = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private SubmissionStatus overallStatus;
    private String overallScore;
    private Instant submissionTime;
    private Long userId;

    public SubmissionEntity() {}

    public SubmissionEntity(Long submissionId, List<ProblemSubmissionEntity> problems, SubmissionStatus overallStatus, String overallScore, Instant submissionTime, Long userId) {
        this.submissionId = submissionId;
        this.problems = problems;
        this.overallStatus = overallStatus;
        this.overallScore = overallScore;
        this.submissionTime = submissionTime;
        this.userId = userId;
    }

    public Long getSubmissionId() {return submissionId;}
    public void setSubmissionId(Long submissionId) {this.submissionId = submissionId;}
    public List<ProblemSubmissionEntity> getProblems() {return problems;}
    public void setProblems(List<ProblemSubmissionEntity> problems) {this.problems = problems;}
    public SubmissionStatus getOverallStatus() {return overallStatus;}
    public void setOverallStatus(SubmissionStatus overallStatus) {this.overallStatus = overallStatus;}
    public String getOverallScore() {return overallScore;}
    public void setOverallScore(String overallScore) {this.overallScore = overallScore;}
    public Instant getSubmissionTime() {return submissionTime;}
    public void setSubmissionTime(Instant submissionTime) {this.submissionTime = submissionTime;}
    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}
}