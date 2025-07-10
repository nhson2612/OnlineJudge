package org.example.judge.problem.model;

import jakarta.persistence.*;
import org.example.judge.exam.Exam;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

}
