package org.example.judge.auth.domain;

public interface SearchUserProjection {
    Long getId();
    String getUsername();
    String getFullName();
    Role getRole();
}
