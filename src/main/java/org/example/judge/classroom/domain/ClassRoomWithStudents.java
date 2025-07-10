package org.example.judge.classroom.domain;

import org.example.judge.auth.domain.Role;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface ClassRoomWithStudents {
    Long getId();
    String getName();

    @Value("#{@userRepository.findByClassRoomId(target.id)}")
    List<StudentProjection> getStudents();

    interface StudentProjection {
        Long getStudentId();
        String getUserName();
        Role getRole();
    }
}