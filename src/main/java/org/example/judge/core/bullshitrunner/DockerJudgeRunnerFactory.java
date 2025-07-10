package org.example.judge.core.bullshitrunner;

public class DockerJudgeRunnerFactory {
    public static DockerJudgeRunner create(String language) {
        switch (language.toLowerCase()) {
            case "cpp":
                return new CppJudgeRunner();
            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }
    }
}
