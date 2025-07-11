package org.example.judge.core.factory;

import org.example.judge.core.domain.ProblemSubmission;
import org.example.judge.submission.model.ProblemSubmissionEntity;

import java.util.stream.Collectors;

public class ProblemSubmissionFactory {
    public static ProblemSubmission fromEntity(ProblemSubmissionEntity entity) {
        return new ProblemSubmission(
                entity.getProblemId(),
                entity.getSourceCodePath(),
                entity.getLanguage(),
                entity.getResults().stream().map(
                        TestCaseResultFactory::fromEntity
                ).collect(Collectors.toList()),
                entity.getScore(),
                entity.getTestcaseSet().stream()
                        .map(testcase -> testcase.getInputPath() + "," + testcase.getExpectedOutputPath())
                        .toList()
        );
    }
}