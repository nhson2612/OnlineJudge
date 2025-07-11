package org.example.judge.problem.model;


import jakarta.persistence.*;

@Entity
@Table(
        name = "test_cases",
        uniqueConstraints = @UniqueConstraint(columnNames = {"problem_id","orderIndex"}),
        indexes = {
                @Index(name = "idx_test_cases_problem_id", columnList = "problem_id"),
        }
)
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;
    private Long orderIndex;

    public TestCase() {
    }

    public TestCase(Problem problem, Long orderIndex) {
        this.problem = problem;
        this.orderIndex = orderIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }
}