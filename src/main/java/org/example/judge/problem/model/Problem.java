package org.example.judge.problem.model;

import jakarta.persistence.*;
import org.example.judge.exam.domain.Exam;
import org.example.judge.exam.domain.ProblemReq;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(
        name = "problems",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;           // Tên đề bài
    private String description;    // Mô tả đề bài (Markdown)
    private String inputFormat;    // Mô tả input
    private String outputFormat;   // Mô tả output
    private int timeLimit;         // Giới hạn thời gian (ms)
    private int memoryLimit;       // Giới hạn bộ nhớ (MB)
//    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<TestCase> hiddenTestCases; // Danh sách test case chấm ẩn (hidden)
    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCase> publicTestCases; // Danh sách test case công khai (public)
    private Long createBy;        // ID người tạo
    private Instant createAt;      // Thời gian tạo
    private boolean isFunctionOnly; // Chỉ sử dụng hàm (function only)
    @Lob
    private String mainTemplate; // // phần main dùng để test hàm
    @ManyToMany
    @JoinTable(
            name = "problem_topic",
            joinColumns = @JoinColumn(name = "problem_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private Set<Topic> topics = new HashSet<>();
    @ManyToMany(mappedBy = "problems")
    private Set<Exam> exams; // Các kỳ thi liên quan đến đề bài
    private String inputFileName;
    private String expectedOutputFileName;

    public Problem(ProblemReq problemReq) {
        this.id = problemReq.id();
        this.name = problemReq.name();
        this.description = problemReq.description();
        this.inputFormat = problemReq.inputFormat();
        this.outputFormat = problemReq.outputFormat();
        this.timeLimit = problemReq.timeLimit();
        this.memoryLimit = problemReq.memoryLimit();
        this.createBy = problemReq.createBy();
        this.createAt = Instant.now();
        this.isFunctionOnly = problemReq.isFunctionOnly();
        this.mainTemplate = problemReq.mainTemplate();
        if (problemReq.topics() != null) {
            this.topics.addAll(problemReq.topics().stream().map(Topic::new).collect(Collectors.toSet()));
        }
        this.publicTestCases = problemReq.publicTestCaseIds().stream()
                .map(testCaseId -> new TestCase(this, testCaseId))
                .collect(Collectors.toList());
    }

    public Problem() {

    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getInputFormat() {return inputFormat;}
    public void setInputFormat(String inputFormat) {this.inputFormat = inputFormat;}
    public String getOutputFormat() {return outputFormat;}
    public void setOutputFormat(String outputFormat) {this.outputFormat = outputFormat;}
    public int getTimeLimit() {return timeLimit;}
    public void setTimeLimit(int timeLimit) {this.timeLimit = timeLimit;}
    public int getMemoryLimit() {return memoryLimit;}
    public void setMemoryLimit(int memoryLimit) {this.memoryLimit = memoryLimit;}
    public List<TestCase> getPublicTestCases() {return publicTestCases;}
    public void setPublicTestCases(List<TestCase> publicTestCases) {this.publicTestCases = publicTestCases;}
    public Long getCreateBy() {return createBy;}
    public void setCreateBy(Long createBy) {this.createBy = createBy;}
    public Instant getCreateAt() {return createAt;}
    public void setCreateAt(Instant createAt) {this.createAt = createAt;}
    public boolean isFunctionOnly() {return isFunctionOnly;}
    public void setFunctionOnly(boolean functionOnly) {isFunctionOnly = functionOnly;}
    public String getMainTemplate() {return mainTemplate;}
    public void setMainTemplate(String mainTemplate) {this.mainTemplate = mainTemplate;}
    public Set<Topic> getTopics() {return topics;}
    public void setTopics(Set<Topic> topics) {this.topics = topics;}
    public Set<Exam> getExams() {return exams;}
    public void setExams(Set<Exam> exams) {this.exams = exams;}
    public String getInputFileName() {return inputFileName;}
    public void setInputFileName(String inputFileName) {this.inputFileName = inputFileName;}
    public String getExpectedOutputFileName() {return expectedOutputFileName;}
    public void setExpectedOutputFileName(String expectedOutputFileName) {this.expectedOutputFileName = expectedOutputFileName;}
}
