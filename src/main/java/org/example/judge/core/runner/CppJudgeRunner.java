package org.example.judge.core.runner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.judge.core.domain.ExecutionContext;
import org.example.judge.core.domain.JudgeRawResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class CppJudgeRunner implements DockerJudgeRunner {
    private static final long TIMEOUT_SECONDS = 1000;
    private static final int EXIT_CODE_TIMEOUT = 124;
    private static final int EXIT_CODE_COMPILATION_ERROR = 1;
    private static final int EXIT_CODE_INTERNAL_ERROR = -1;
    private static final int MAX_WAIT_FOR_OUTPUT_MS = 5000; // 5 giây chờ output file
    private static final Log LOG = LogFactory.getLog(CppJudgeRunner.class);

    @Override
    public JudgeRawResult rụn(ExecutionContext context) {
        long startTime = System.currentTimeMillis();
        Process process = null;
        int order = context.getTestcase().getOrderIndex();
        Path outputPath = null;

        try {
            Path sourcePath = Paths.get(context.getSourceCodePath()).toAbsolutePath().normalize();
            Path inputPath = Paths.get(context.getTestcase().getInputPath()).toAbsolutePath().normalize();
            Path workDir = sourcePath.getParent();

            // Xác định đường dẫn output file trước khi chạy
            outputPath = workDir.resolve("out" + order + ".txt");

            // Xóa file output cũ nếu có
            if (Files.exists(outputPath)) {
                Files.delete(outputPath);
                LOG.info("Deleted existing output file: " + outputPath);
            }

            LOG.info("Docker command: docker run --rm -v " + inputPath.toString() + ":/input/" + inputPath.getFileName().toString() +
                    " -v " + workDir.toString() + ":/workdir --workdir /workdir --memory 512m --cpus 1 judge-cpp " +
                    sourcePath.getFileName().toString() + " " + inputPath.getFileName().toString() + " " + order);

            ProcessBuilder builder = new ProcessBuilder(
                    "docker", "run", "--rm",
                    "-v", inputPath.toString() + ":/input/" + inputPath.getFileName().toString(),
                    "-v", workDir.toString() + ":/workdir",
                    "--workdir", "/workdir",
                    "--memory", "512m",
                    "--cpus", "1",
                    "judge-cpp",
                    sourcePath.getFileName().toString(),
                    inputPath.getFileName().toString(),
                    String.valueOf(order)
            );

            builder.redirectErrorStream(true);
            process = builder.start();

            // Chờ process hoàn thành với timeout
            boolean finished = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            long execTime = System.currentTimeMillis() - startTime;

            if (!finished) {
                process.destroyForcibly();
                return createErrorResult(EXIT_CODE_TIMEOUT, startTime);
            }

            int exitCode = process.exitValue();
            LOG.info("Docker process finished with exit code: " + exitCode);

            // Chờ và kiểm tra file output được tạo ra
            if (!waitForOutputFile(outputPath, MAX_WAIT_FOR_OUTPUT_MS)) {
                LOG.error("Output file not found after Docker execution: " + outputPath);
                return createErrorResult(EXIT_CODE_INTERNAL_ERROR, startTime);
            }

            // Kiểm tra file output có thể đọc được không
            if (!Files.isReadable(outputPath)) {
                LOG.error("Output file is not readable: " + outputPath);
                return createErrorResult(EXIT_CODE_INTERNAL_ERROR, startTime);
            }

            LOG.info("Output file successfully created: " + outputPath);
            return new JudgeRawResult(outputPath.toString(), execTime, 0, exitCode);

        } catch (IOException e) {
            LOG.error("IOException during Docker execution", e);
            return createErrorResult(EXIT_CODE_INTERNAL_ERROR, startTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.error("Docker execution interrupted", e);
            return createErrorResult(EXIT_CODE_INTERNAL_ERROR, startTime);
        } catch (Exception e) {
            LOG.error("Unexpected error during Docker execution", e);
            return createErrorResult(EXIT_CODE_INTERNAL_ERROR, startTime);
        } finally {
            if (process != null) {
                process.destroyForcibly();
            }
        }
    }

    /**
     * Chờ file output được tạo ra trong khoảng thời gian cho phép
     * @param outputPath đường dẫn đến file output
     * @param maxWaitMs thời gian chờ tối đa (milliseconds)
     * @return true nếu file được tạo ra, false nếu timeout
     */
    private boolean waitForOutputFile(Path outputPath, long maxWaitMs) {
        long startWait = System.currentTimeMillis();
        long endWait = startWait + maxWaitMs;

        while (System.currentTimeMillis() < endWait) {
            if (Files.exists(outputPath) && Files.isReadable(outputPath)) {
                try {
                    // Kiểm tra file có size > 0 và có thể đọc được
                    if (Files.size(outputPath) >= 0) {
                        LOG.info("Output file found and readable: " + outputPath +
                                " (size: " + Files.size(outputPath) + " bytes)");
                        return true;
                    }
                } catch (IOException e) {
                    LOG.warn("Error checking output file size: " + e.getMessage());
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        return false;
    }

    private JudgeRawResult createErrorResult(int exitCode, long startTime) {
        long execTime = System.currentTimeMillis() - startTime;
        return new JudgeRawResult(null, execTime, 0, exitCode);
    }
}