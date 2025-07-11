package org.example.judge.core.factory;
import org.example.judge.core.domain.TestCaseResult;
import org.example.judge.submission.model.TestCaseResultEntity;

public class TestCaseResultFactory {
    public static TestCaseResult fromEntity(TestCaseResultEntity entity) {
        return new org.example.judge.core.domain.TestCaseResult(
                entity.getType(),
                entity.getActualOutputPath(),
                entity.getExpectedOutputPath(),
                entity.getExecutionTime(),
                entity.getMemoryUsage()
        );
    }
}
