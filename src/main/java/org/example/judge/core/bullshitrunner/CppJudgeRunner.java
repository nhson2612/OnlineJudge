package org.example.judge.core.bullshitrunner;

import org.example.judge.submission.model.old.JudgeRawResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class CppJudgeRunner implements DockerJudgeRunner {
    private static final long TIMEOUT_SECONDS = 10;
    private static final int EXIT_CODE_TIMEOUT = 124;
    private static final int EXIT_CODE_COMPILATION_ERROR = 1;
    private static final int EXIT_CODE_INTERNAL_ERROR = -1;

    @Override
    public JudgeRawResult rụn(String sourceCodePath, String inputPath) {
        long startTime = System.currentTimeMillis();
        Process process = null;

        try {
            Path sourcePath = Paths.get(sourceCodePath).toAbsolutePath().normalize();
            Path inputPathAbs = Paths.get(inputPath).toAbsolutePath().normalize();
            Path workDir = sourcePath.getParent();

            // Validate file existence
            if (!Files.exists(sourcePath)) {
                return createErrorResult("Source file not found: " + sourcePath,
                        EXIT_CODE_INTERNAL_ERROR, startTime);
            }
            if (!Files.exists(inputPathAbs)) {
                return createErrorResult("Input file not found: " + inputPathAbs,
                        EXIT_CODE_INTERNAL_ERROR, startTime);
            }

            // Set up Docker command
            ProcessBuilder builder = new ProcessBuilder(
                    "docker", "run", "--rm",
                    "-v", inputPathAbs.toString() + ":/input/" + inputPathAbs.getFileName().toString(),
                    "-v", workDir.toString() + ":/workdir",
                    "--workdir", "/workdir",
                    "--memory", "512m",
                    "--cpus", "1",
                    "judge-cpp",
                    sourcePath.getFileName().toString(),
                    inputPathAbs.getFileName().toString()
            );
            builder.redirectErrorStream(true);
            process = builder.start();

            // Read output
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // Wait for process completion with timeout
            boolean finished = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            long execTime = System.currentTimeMillis() - startTime;

            if (!finished) {
                process.destroyForcibly();
                return createErrorResult("Time limit exceeded (> " + TIMEOUT_SECONDS + "s)",
                        EXIT_CODE_TIMEOUT, startTime);
            }

            int exitCode = process.exitValue();
            String outputStr = output.toString().trim();

            // Handle different exit codes
            if (exitCode == EXIT_CODE_COMPILATION_ERROR) {
                return new JudgeRawResult(exitCode, "", "Compilation Error: " + outputStr, execTime);
            } else if (exitCode == EXIT_CODE_TIMEOUT) {
                return new JudgeRawResult(exitCode, outputStr, "Time Limit Exceeded", execTime);
            } else if (exitCode != 0) {
                return new JudgeRawResult(exitCode, outputStr,
                        "Runtime Error (exit code: " + exitCode + ")", execTime);
            }

            return new JudgeRawResult(exitCode, outputStr, "", execTime);

        } catch (IOException e) {
            return createErrorResult("IO Error: " + e.getMessage(),
                    EXIT_CODE_INTERNAL_ERROR, startTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            return createErrorResult("Execution interrupted: " + e.getMessage(),
                    EXIT_CODE_INTERNAL_ERROR, startTime);
        } catch (Exception e) {
            return createErrorResult("Unexpected error: " + e.getMessage(),
                    EXIT_CODE_INTERNAL_ERROR, startTime);
        } finally {
            if (process != null) {
                process.destroyForcibly();
            }
        }
    }

    private JudgeRawResult createErrorResult(String errorMessage, int exitCode, long startTime) {
        long execTime = System.currentTimeMillis() - startTime;
        return new JudgeRawResult(exitCode, "", errorMessage, execTime);
    }

}