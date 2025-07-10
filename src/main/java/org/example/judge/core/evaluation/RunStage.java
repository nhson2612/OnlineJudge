package org.example.judge.core.evaluation;

import org.example.judge.core.domain.ExecutionContext;
import org.example.judge.core.domain.JudgeRawResult;
import org.example.judge.core.domain.OutputContext;
import org.example.judge.core.runner.DockerJudgeRunner;
import org.example.judge.core.runner.RunnerFactory;

public class RunStage implements Stage<ExecutionContext, OutputContext> {
    private DockerJudgeRunner runner;

    public RunStage(String language) {
        this.runner = RunnerFactory.create(language);
    }

    @Override
    public OutputContext process(ExecutionContext input){
        JudgeRawResult judgeRawResult = runner.rụn(input);
        return new OutputContext(
                judgeRawResult.getOutputPath(),
                input.getTestcase().getExpectedOutputPath(),
                judgeRawResult.getExecutionTime(),
                judgeRawResult.getMemoryUsage(),
                judgeRawResult.getExitCode()
        );
    }
}
