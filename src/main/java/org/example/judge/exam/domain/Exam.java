package org.example.judge.exam.domain;

import jakarta.persistence.*;
import org.example.judge.classroom.domain.ClassRoom;
import org.example.judge.problem.model.Problem;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

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
    private Long updateBy;
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

    public Exam(DraftExamReq draftExamReq) {
        this.id = draftExamReq.id();
        this.name = draftExamReq.name();
        this.startTime = draftExamReq.startTime();
        this.endTime = draftExamReq.endTime();
        this.status = ExamStatus.NOT_STARTED;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.classRooms = draftExamReq.classRoomIds().stream()
                .map(id -> {
                    ClassRoom c = new ClassRoom();
                    c.setId(id);
                    return c;
                })
                .collect(Collectors.toSet());
        this.problems = draftExamReq.problemReqs().stream().map(Problem::new).collect(Collectors.toSet());
        this.isDraft = true;
    }

    public Exam() {

    }

    public Long getId() {return id;}
    public String getName() {return name;}
    public Instant getStartTime() {return startTime;}
    public Instant getEndTime() {return endTime;}
    public ExamStatus getStatus() {return status;}
    public Instant getCreatedAt() {return createdAt;}
    public Instant getUpdatedAt() {return updatedAt;}
    public Long getUpdateBy() {return updateBy;}
    public Set<ClassRoom> getClassRooms() {return classRooms;}
    public Set<Problem> getProblems() {return problems;}
    public boolean isDraft() {return isDraft;}

    public void setId(Long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setStartTime(Instant startTime) {this.startTime = startTime;}
    public void setEndTime(Instant endTime) {this.endTime = endTime;}
    public void setStatus(ExamStatus status) {this.status = status;}
    public void setCreatedAt(Instant createdAt) {this.createdAt = createdAt;}
    public void setUpdatedAt(Instant updatedAt) {this.updatedAt = updatedAt;}
    public void setUpdateBy(Long updateBy) {this.updateBy = updateBy;}
    public void setClassRooms(Set<ClassRoom> classRooms) {this.classRooms = classRooms;}
    public void setProblems(Set<Problem> problems) {this.problems = problems;}
    public void setDraft(boolean draft) {isDraft = draft;}
}
