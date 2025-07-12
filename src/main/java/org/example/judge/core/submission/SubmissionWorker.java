package org.example.judge.core.submission;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.judge.core.evaluation.JudgePipelineException;
import org.example.judge.core.domain.SubmissionJob;
import org.example.judge.core.domain.SubmissionStatus;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SubmissionWorker {
    private static final Log LOG = LogFactory.getLog(SubmissionWorker.class);
    private final ExecutorService executorService;
    private final SubmissionJobQueue submissionJobQueue;
    private final AtomicInteger processingCount;
    private final int maxConcurrentSubmissions;
    private volatile boolean isRunning;
    private final SubmissionEvaluator evaluator;
    private Consumer<SubmissionJob> onCompleteCallback = job -> {};

    public SubmissionWorker(int maxConcurrentSubmissions, int maxQueueSize, SubmissionEvaluator evaluator) {
        this.maxConcurrentSubmissions = maxConcurrentSubmissions;
        this.executorService = java.util.concurrent.Executors.newFixedThreadPool(maxConcurrentSubmissions, r -> {
            Thread t = new Thread(r, "Submission-Worker-" + System.currentTimeMillis());
            t.setDaemon(false);
            return t;
        });
        this.submissionJobQueue = new SubmissionJobQueue(maxQueueSize);
        this.evaluator = evaluator;
        this.processingCount = new AtomicInteger(0);
        this.isRunning = false;
    }
    public void setOnCompleteCallback(Consumer<SubmissionJob> callback) {
        this.onCompleteCallback = callback;
    }
    public void start() {
            if (isRunning) {
                return;
            }
            isRunning = true;
            LOG.info("🚀 Starting Async Judge System with " + maxConcurrentSubmissions + " workers");
            for (int i = 0; i < maxConcurrentSubmissions; i++) {
                executorService.submit(this::workerLoop);
            }
    }

    public void stop() {
        isRunning = false;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        LOG.info("⏹️ Judge system stopped");
    }
    private void workerLoop() {
        while (isRunning) {
            try {
                SubmissionJob submission = submissionJobQueue.takeSubmission();
                processSubmission(submission);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                LOG.error("❌ Worker error: " + e.getMessage());
            }
        }
    }
    private void processSubmission(SubmissionJob submission) {
        String submissionId = submission.getSubmissionId();
        int currentRunning = processingCount.incrementAndGet();

        try {
            LOG.info("🔄 Processing submission: " + submissionId + " (Running: " + currentRunning + ")");
            submission.setOverallStatus(SubmissionStatus.RUNNING);
            SubmissionJob submissionJob = evaluator.execute(submission);
            submissionJobQueue.completeSubmission(Long.valueOf(submissionId), submissionJob);
            LOG.info("🎉🎉🎉 processing success : " + submissionId + " your overall score is " + submissionJob.getOverallScore());
        } catch (JudgePipelineException e) {
            if(e.getExitCode()==-1){
                LOG.error("❌ Submission " + submissionId + " failed due to missing files: " + e.getMessage());
                submission.setOverallStatus(SubmissionStatus.FAILED);
            }
            LOG.error("❌ Submission " + submissionId + " failed: " + e.getMessage());
            submission.setOverallStatus(SubmissionStatus.FAILED);
        } finally {
            processingCount.decrementAndGet();
            try {
                onCompleteCallback.accept(submission);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public CompletableFuture<SubmissionJob> submitAsync(SubmissionJob submission) {
        return submissionJobQueue.submit(submission);
    }
    public int getQueueSize() {
        return submissionJobQueue.getQueueSize();
    }

    public int getRunningJudges() {
        return processingCount.get();
    }

    public long getTotalProcessed() {
        return submissionJobQueue.getTotalProcessed();
    }

    public int getRemainingCapacity() {return submissionJobQueue.getAvailableSlots();}
    public void printStats() {
        LOG.info("📊 Judge System Stats:");
        LOG.info("  Queue Size: " + getQueueSize());
        LOG.info("  Running Judges: " + getRunningJudges());
        LOG.info("  Total Processed: " + getTotalProcessed());
    }    
}
