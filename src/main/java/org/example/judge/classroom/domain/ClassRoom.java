package org.example.judge.classroom.domain;

import jakarta.persistence.*;
import org.example.judge.auth.domain.User;

import java.util.HashSet;
import java.util.List;
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
    public void setId(Long id) {this.id = id;}
    public void setStudents(Set<User> students) {this.students = students;}

    public ClassRoom(Long id, String name, Set<User> students) {
        this.id = id;
        this.name = name;
        this.students = students;
    }
    public ClassRoom() { } // Default constructor for JPA
}
