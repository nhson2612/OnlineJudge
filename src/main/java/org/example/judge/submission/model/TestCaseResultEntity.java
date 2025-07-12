package org.example.judge.submission.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.example.judge.core.domain.TestCaseResult;
import org.example.judge.core.domain.TestcaseResultType;

@Entity
@Table(name = "test_case_results")
public class TestCaseResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long testCaseId;
    @Enumerated(EnumType.STRING)
    private TestcaseResultType type;
    private String actualOutputPath;
    private String expectedOutputPath;
    private long executionTime;
    private long memoryUsage;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_submission_id")
    @JsonBackReference
    private ProblemSubmissionEntity problemSubmission;

    public TestCaseResultEntity() {}
    public TestCaseResultEntity(Long id,Long testCaseId, TestcaseResultType type, String actualOutputPath, String expectedOutputPath, long executionTime, long memoryUsage) {
        this.id = id;
        this.testCaseId = testCaseId;
        this.type = type;
        this.actualOutputPath = actualOutputPath;
        this.expectedOutputPath = expectedOutputPath;
        this.executionTime = executionTime;
        this.memoryUsage = memoryUsage;
    }

    public TestCaseResultEntity(TestCaseResult testCaseResult){
        this.type = testCaseResult.getType();
        this.actualOutputPath = testCaseResult.getActualOutputPath();
        this.expectedOutputPath = testCaseResult.getExpectedOutputPath();
        this.executionTime = testCaseResult.getExecutionTime();
        this.memoryUsage = testCaseResult.getMemoryUsage();
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Long getTestCaseId() {return testCaseId;}
    public void setTestCaseId(Long testCaseId) {this.testCaseId = testCaseId;}
    public TestcaseResultType getType() {return type;}
    public void setType(TestcaseResultType type) {this.type = type;}
    public String getActualOutputPath() {return actualOutputPath;}
    public void setActualOutputPath(String actualOutputPath) {this.actualOutputPath = actualOutputPath;}
    public String getExpectedOutputPath() {return expectedOutputPath;}
    public void setExpectedOutputPath(String expectedOutputPath) {this.expectedOutputPath = expectedOutputPath;}
    public long getExecutionTime() {return executionTime;}
    public void setExecutionTime(long executionTime) {this.executionTime = executionTime;}
    public long getMemoryUsage() {return memoryUsage;}
    public void setMemoryUsage(long memoryUsage) {this.memoryUsage = memoryUsage;}
    public ProblemSubmissionEntity getProblemSubmission() {return problemSubmission;}
    public void setProblemSubmission(ProblemSubmissionEntity problemSubmission) {this.problemSubmission = problemSubmission;}
}