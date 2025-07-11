package org.example.judge.auth;

public enum Role {
    ADMIN,
    STUDENT,
    TEACHER;

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isStudent() {
        return this == STUDENT;
    }

    public boolean isTeacher() {
        return this == TEACHER;
    }
}
