package org.example.judge.core.runner;

public class RunnerFactory {
    public static DockerJudgeRunner create(String language) {
        switch (language.toLowerCase()) {
            case "cpp":
                return new CppJudgeRunner();
            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }
    }
}