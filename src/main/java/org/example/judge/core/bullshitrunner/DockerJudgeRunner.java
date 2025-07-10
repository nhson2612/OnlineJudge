package org.example.judge.core.bullshitrunner;

import org.example.judge.submission.model.old.JudgeRawResult;

public interface DockerJudgeRunner {
    JudgeRawResult rụn(String sourceCodePath, String inputPath);
}
