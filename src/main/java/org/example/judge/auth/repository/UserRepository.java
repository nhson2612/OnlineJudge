package org.example.judge.auth.repository;

import org.example.judge.auth.domain.SearchUserProjection;
import org.example.judge.auth.domain.User;
import org.example.judge.classroom.domain.ClassRoomWithStudents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    @Query("SELECT u.id as studentId, u.username as userName, u.role as role " +
            "FROM User u JOIN u.classes c WHERE c.id = :classRoomId")
    List<ClassRoomWithStudents.StudentProjection> findByClassRoomId(@Param("classRoomId") Long classRoomId);
    @Query(value = "SELECT u.id as id, u.username as username, u.fullName as fullName, u.role as role " +
            "FROM User u WHERE MATCH(username, fullName) AGAINST (:name IN BOOLEAN MODE)", nativeQuery = true)
    List<SearchUserProjection> fullTextSearch(String name);
}
