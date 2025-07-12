package org.example.judge.submission.repository;

import org.example.judge.submission.model.TestCaseResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCaseResultEntityRepository extends JpaRepository<TestCaseResultEntity,Long> {
}
