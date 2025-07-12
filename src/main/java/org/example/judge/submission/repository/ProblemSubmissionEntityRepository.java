package org.example.judge.submission.repository;

import org.example.judge.submission.model.ProblemSubmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemSubmissionEntityRepository extends JpaRepository<ProblemSubmissionEntity,Long> {
}
