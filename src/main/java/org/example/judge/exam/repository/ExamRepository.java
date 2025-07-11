package org.example.judge.exam.repository;

import org.example.judge.exam.domain.Exam;
import org.example.judge.exam.domain.ExamResponse;
import org.example.judge.exam.domain.StudentExamView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam,Long> {
    List<ExamResponse> findAllByIsDraftTrueAndUpdateBy(Long userId);
    Optional<ExamResponse> findByIdAndIsDraftTrueAndUpdateBy(Long id, Long userId);
    @Query("SELECT e FROM Exam e JOIN e.classRooms c WHERE e.isDraft = false AND c.id IN :classIds")
    List<StudentExamView> findAllAvailableExamsForStudent(@Param("classIds") List<Long> classIds);
    @Query("SELECT e FROM Exam e WHERE e.id = :id AND e.isDraft = false")
    Optional<ExamResponse> findExamResponseById(@Param("id") Long id);
}
