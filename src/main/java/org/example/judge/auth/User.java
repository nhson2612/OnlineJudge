package org.example.judge.auth;

import jakarta.persistence.*;
import org.example.judge.classroom.ClassRoom;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_users_username", columnList = "username"),
                @Index(name = "idx_users_email", columnList = "email")
        }
)
public class User {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String hashedPassword;
    private String fullName;
    private Role role;
    private Instant createdAt;
    private Instant updatedAt;
    @ManyToMany(mappedBy = "students")
    private Set<ClassRoom> classes ; // Danh sách lớp học mà người dùng tham gia

    public void enroll(ClassRoom classRoom){
        classes.add(classRoom);
        classRoom.getStudents().add(this);
    }
    public void unenroll(ClassRoom classRoom) {
        classes.remove(classRoom);
        classRoom.getStudents().remove(this);
    }

    public Long getId() {return id;}

    public String getUsername() {return username;}

    public String getHashedPassword() {return hashedPassword;}

    public String getFullName() {return fullName;}

    public Role getRole() {return role;}

    public Instant getCreatedAt() {return createdAt;}

    public Instant getUpdatedAt() {return updatedAt;}

    public Set<ClassRoom> getClasses() {return classes;}
}