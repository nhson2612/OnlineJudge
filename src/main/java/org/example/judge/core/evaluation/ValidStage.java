package org.example.judge.core.evaluation;

import org.example.judge.core.domain.ExecutionContext;

import java.nio.file.Files;
import java.nio.file.Path;

public class ValidStage implements Stage<ExecutionContext, ExecutionContext> {
    @Override
    public ExecutionContext process(ExecutionContext input) {
        if (!Files.exists(Path.of(input.getSourceCodePath()))) {
            throw new JudgePipelineException("Source file not found: " + input.getSourceCodePath() ,-1);
        }
        if (!Files.exists(Path.of(input.getTestcase().getInputPath()))) {
            throw new JudgePipelineException("Testcase input file not found: " + input.getTestcase().getInputPath(), -1);
        }
        if (!Files.exists(Path.of(input.getTestcase().getExpectedOutputPath()))) {
            throw new JudgePipelineException("Testcase output file not found: " + input.getTestcase().getExpectedOutputPath(), -1);
        }
        return input;
    }
}
