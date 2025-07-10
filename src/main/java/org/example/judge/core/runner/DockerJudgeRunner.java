package org.example.judge.core.runner;

import org.example.judge.core.domain.ExecutionContext;
import org.example.judge.core.domain.JudgeRawResult;

public interface DockerJudgeRunner {
    JudgeRawResult rụn(ExecutionContext executionContext);
}
