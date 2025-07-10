package org.example.judge.problem.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // Ví dụ: "Array", "DP", "Sorting"
    @ManyToMany(mappedBy = "topics")
    private Set<Problem> problems = new HashSet<>();

}
