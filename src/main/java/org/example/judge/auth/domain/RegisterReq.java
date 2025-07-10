package org.example.judge.auth.domain;

public record RegisterReq(String username, String password) {
    public RegisterReq {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
    }
}
