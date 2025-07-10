package org.example.judge.classroom.repostiroty;

import org.example.judge.classroom.domain.ClassRoom;
import org.example.judge.classroom.domain.ClassRoomWithStudents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    @Query("SELECT c.id as id, c.name as name FROM ClassRoom c WHERE c.id = :id")
    Optional<ClassRoomWithStudents> find(@Param("id") Long id);
    @Query("SELECT c.id as id, c.name as name FROM ClassRoom c")
    List<ClassRoomWithStudents> findBy();
}
