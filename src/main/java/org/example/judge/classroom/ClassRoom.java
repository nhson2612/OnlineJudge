package org.example.judge.classroom;

import jakarta.persistence.*;
import org.example.judge.auth.User;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class ClassRoom {
    @Id
    private Long id;
    private String name;
    @ManyToMany
    @JoinTable(
            name = "classroom_student",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> students = new HashSet<>();

    public Set<User> getStudents() { return students;}
    public String getName() { return name;}
    public void setName(String name) { this.name = name;}
    public Long getId() { return id;}
}
