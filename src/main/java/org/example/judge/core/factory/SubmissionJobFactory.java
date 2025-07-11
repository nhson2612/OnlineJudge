package org.example.judge.core.factory;

import org.example.judge.core.domain.ProblemSubmission;
import org.example.judge.submission.model.SubmissionEntity;
import org.example.judge.core.domain.SubmissionJob;

import java.util.List;

public class SubmissionJobFactory {
    public static SubmissionJob fromEntity(SubmissionEntity entity) {
        List<ProblemSubmission> problems = entity.getProblems().stream()
                .map(ProblemSubmissionFactory::fromEntity)
                .toList();
        return new SubmissionJob(
                entity.getSubmissionId().toString(),
                problems,
                entity.getOverallStatus(),
                entity.getOverallScore()
        );
    }
}
