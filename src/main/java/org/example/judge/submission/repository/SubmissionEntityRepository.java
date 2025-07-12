package org.example.judge.submission.repository;

import org.example.judge.submission.model.SubmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionEntityRepository extends JpaRepository<SubmissionEntity, Long> {
    @Query(value = """
    SELECT * FROM submissions
    WHERE overall_status = 'PENDING'
    ORDER BY RAND()
    LIMIT :limit
""", nativeQuery = true)
    List<SubmissionEntity> findRandomPendingSubmissions(@Param("limit") int limit);
}