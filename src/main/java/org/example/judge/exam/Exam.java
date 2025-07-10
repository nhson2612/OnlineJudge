package org.example.judge.exam;

import jakarta.persistence.*;
import org.example.judge.classroom.ClassRoom;
import org.example.judge.problem.model.Problem;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(
        name = "exams",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Instant startTime;
    private Instant endTime;
    @Enumerated(EnumType.STRING)
    private ExamStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant updateBy;
    @ManyToMany
    @JoinTable(
            name = "exam_classrooms",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "classroom_id")
    )
    private Set<ClassRoom> classRooms;
    @ManyToMany
    @JoinTable(name = "exam_problem",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "problem_id"))
    private Set<Problem> problems;
    private boolean isDraft;

    public Long getId() {return id;}
    public String getName() {return name;}
    public Instant getStartTime() {return startTime;}
    public Instant getEndTime() {return endTime;}
    public ExamStatus getStatus() {return status;}
    public Instant getCreatedAt() {return createdAt;}
    public Instant getUpdatedAt() {return updatedAt;}
    public Instant getUpdateBy() {return updateBy;}
    public Set<ClassRoom> getClassRooms() {return classRooms;}
    public Set<Problem> getProblems() {return problems;}
    public boolean isDraft() {return isDraft;}
}
