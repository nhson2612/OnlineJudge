package org.example.judge.core.evaluation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.judge.core.domain.OutputContext;
import org.example.judge.core.domain.TestCaseResult;
import org.example.judge.core.domain.TestcaseResultType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CompareStage implements Stage<OutputContext, TestCaseResult> {

    private static final Log LOG = LogFactory.getLog(CompareStage.class);

    @Override
    public TestCaseResult process(OutputContext input) {
        TestCaseResult result = new TestCaseResult();

        result.setActualOutputPath(input.getActualOutputPath());
        result.setExpectedOutputPath(input.getExpectedOutputPath());
        result.setExecutionTime(input.getExecutionTime());
        result.setMemoryUsage(input.getMemoryUsage());

        try {
            switch (input.getExitCode()) {
                case 0:
                    boolean isEqual = Files.mismatch(
                            Path.of(input.getActualOutputPath()),
                            Path.of(input.getExpectedOutputPath())
                    ) == -1;

                    result.setType(isEqual ? TestcaseResultType.PASSED : TestcaseResultType.FAILED);
                    break;

                case 124:
                    result.setType(TestcaseResultType.TIMEOUT);
                    break;

                case 1:
                    result.setType(TestcaseResultType.COMPILATION_ERROR);
                    break;

                default:
                    result.setType(TestcaseResultType.INTERNAL_ERROR);
                    break;
            }
        } catch (IOException e) {
            result.setType(TestcaseResultType.INTERNAL_ERROR);
            LOG.error("❌ CompareStage error: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
}
