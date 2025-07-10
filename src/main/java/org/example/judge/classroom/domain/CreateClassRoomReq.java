package org.example.judge.classroom.domain;

import java.util.List;

public record CreateClassRoomReq(String name, List<Long> studentIds) {

    public CreateClassRoomReq {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Classroom name cannot be null or blank");
        }
        if (studentIds == null || studentIds.isEmpty()) {
            throw new IllegalArgumentException("Student IDs cannot be null or empty");
        }
    }
}
