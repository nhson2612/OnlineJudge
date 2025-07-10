package org.example.judge.auth.domain;

import jakarta.persistence.*;
import org.example.judge.classroom.domain.ClassRoom;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_users_email", columnList = "email")
        }
)
public class User implements UserDetails {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String fullName;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Instant createdAt;
    private Instant updatedAt;
    @ManyToMany(mappedBy = "students")
    private Set<ClassRoom> classes ; // Danh sách lớp học mà người dùng tham gia

    public User(){} // default constructor for JPA

    public User(Long id, String username, String password, String fullName, Role role, Instant createdAt, Instant updatedAt, Set<ClassRoom> classes) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.classes = classes;
    }

    public Long getId() {return id;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public String getUsername() {return username;}
    @Override
    public String getPassword() {return password;}

    public String getFullName() {return fullName;}

    public Role getRole() {return role;}

    public Instant getCreatedAt() {return createdAt;}

    public Instant getUpdatedAt() {return updatedAt;}

    public Set<ClassRoom> getClasses() {return classes;}

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {}

    public void setRole(Role role) {
        this.role = role;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setClasses(Set<ClassRoom> classes) {
        this.classes = classes;
    }
}