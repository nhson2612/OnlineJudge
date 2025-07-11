//package org.example.judge.core;
//
//import org.example.judge.core.runner.DockerJudgeRunner;
//import org.example.judge.core.runner.DockerJudgeRunnerFactory;
//import org.example.judge.submission.model.old.JudgeRawResult;
//import org.example.judge.submission.model.old.JudgeResult;
//import org.example.judge.submission.model.old.Submission;
//import org.example.judge.core.domain.SubmissionStatus;
//
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class AsyncDockerJudgeRunner {
//    private final ExecutorService judgeExecutor;
//    private final JudgeQueueManager queueManager;
//    private final AtomicInteger runningJudges;
//    private final int maxConcurrentJudges;
//    private volatile boolean isRunning;
//
//    public AsyncDockerJudgeRunner(int maxConcurrentJudges, int maxQueueSize) {
//        this.maxConcurrentJudges = maxConcurrentJudges;
//        this.judgeExecutor = Executors.newFixedThreadPool(maxConcurrentJudges, r -> {
//            Thread t = new Thread(r, "Judge-Worker-" + System.currentTimeMillis());
//            t.setDaemon(true);
//            return t;
//        });
//        this.queueManager = new JudgeQueueManager(maxQueueSize);
//        this.runningJudges = new AtomicInteger(0);
//        this.isRunning = false;
//    }
//
//    public void start() {
//        if (isRunning) {
//            return;
//        }
//        isRunning = true;
//        System.out.println("🚀 Starting Async Judge System with " + maxConcurrentJudges + " workers");
//        for (int i = 0; i < maxConcurrentJudges; i++) {
//            judgeExecutor.submit(this::workerLoop);
//        }
//    }
//
//    public void stop() {
//        isRunning = false;
//        judgeExecutor.shutdown();
//        try {
//            if (!judgeExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
//                judgeExecutor.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            judgeExecutor.shutdownNow();
//            Thread.currentThread().interrupt();
//        }
//        System.out.println("⏹️ Judge system stopped");
//    }
//
//    private void workerLoop() {
//        while (isRunning) {
//            try {
//                Submission submission = queueManager.takeSubmission();
//                processSubmission(submission);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                break;
//            } catch (Exception e) {
//                System.err.println("❌ Worker error: " + e.getMessage());
//            }
//        }
//    }
//
//    private void processSubmission(Submission submission) {
//        String submissionId = submission.getId();
//        int currentRunning = runningJudges.incrementAndGet();
//
//        try {
//            System.out.println("🔄 Processing submission: " + submissionId + " (Running: " + currentRunning + ")");
//            submission.setStatus(SubmissionStatus.RUNNING);
//            DockerJudgeRunner runner = DockerJudgeRunnerFactory.create(submission.getLanguage());
//            JudgeResult result = runInDocker(submissionId,submission,runner);
//            queueManager.completeSubmission(submissionId, result);
//        } catch (Exception e) {
//            queueManager.failSubmission(submissionId, e);
//        } finally {
//            runningJudges.decrementAndGet();
//        }
//    }
//
//    private JudgeResult runInDocker(String submissionId, Submission submission, DockerJudgeRunner runner) {
//        JudgeRawResult raw = runner.rụn(submission.getSourceCodePath(), submission.getInputDataPath());
//        SubmissionStatus status;
//
//        if (raw.getExitCode() == 0) {
//            status = SubmissionStatus.COMPLETED;
//        } else if (raw.getExitCode() == 124) {
//            status = SubmissionStatus.TIMEOUT;
//        } else if (raw.getStderr().toLowerCase().contains("error:") || raw.getStdout().contains("Compilation Error")) {
//            status = SubmissionStatus.COMPILATION_ERROR;
//        } else {
//            status = SubmissionStatus.FAILED;
//        }
//
//        return new JudgeResult(
//                submissionId,
//                status,
//                raw.getStdout(),
//                raw.getExecutionTime(),
//                raw.getStderr(),
//                raw.getExitCode()
//        );
//    }
//
//    public CompletableFuture<JudgeResult> submitAsync(String submissionId, String sourceCode, String inputData, String language) {
//        Submission submission = new Submission(submissionId, sourceCode, inputData, language);
//        return queueManager.submitAsync(submission);
//    }
//
//    public Submission getSubmission(String submissionId) {
//        return queueManager.getSubmission(submissionId);
//    }
//
//    public int getQueueSize() {
//        return queueManager.getQueueSize();
//    }
//
//    public int getRunningJudges() {
//        return runningJudges.get();
//    }
//
//    public long getTotalProcessed() {
//        return queueManager.getTotalProcessed();
//    }
//
//    public void printStats() {
//        System.out.println("📊 Judge System Stats:");
//        System.out.println("  Queue Size: " + getQueueSize());
//        System.out.println("  Running Judges: " + getRunningJudges());
//        System.out.println("  Total Processed: " + getTotalProcessed());
//    }
//}
