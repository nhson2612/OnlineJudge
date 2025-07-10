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
    private int orderIndex;
}