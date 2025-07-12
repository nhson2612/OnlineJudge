package org.example.judge.core.submission;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.judge.core.domain.SubmissionJob;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SubmissionJobQueue {
    private final BlockingQueue<SubmissionJob> pendingQueue;
    private final AtomicInteger queueSize;
    private final ConcurrentHashMap<String, CompletableFuture<SubmissionJob>> futures;
    private static final Log LOG = LogFactory.getLog(SubmissionJobQueue.class);

    public SubmissionJobQueue(int maxQueueSize) {
        this.pendingQueue = new LinkedBlockingQueue<>(maxQueueSize);
        this.queueSize = new AtomicInteger(0);
        this.futures = new ConcurrentHashMap<>();
    }

    public CompletableFuture<SubmissionJob> submit(SubmissionJob submissionJob) {
        CompletableFuture<SubmissionJob> future = new CompletableFuture<>();

        try {
            if (futures.containsKey(submissionJob.getSubmissionId())) {
                future.completeExceptionally(new IllegalArgumentException("Submission already exists: " + submissionJob.getSubmissionId()));
                return future;
            }
            if (pendingQueue.offer(submissionJob)) {
                futures.put(submissionJob.getSubmissionId(), future);
                queueSize.incrementAndGet();
                LOG.info("📥 Queued submission: " + submissionJob.getSubmissionId() + " (Queue size: " + queueSize.get() + ")");
            } else {
                future.completeExceptionally(new IllegalStateException("Queue is full"));
            }

        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    public SubmissionJob takeSubmission() throws InterruptedException {
        SubmissionJob submission = pendingQueue.take();
        queueSize.decrementAndGet();
        LOG.info("📤 Processing submission: " + submission.getSubmissionId() + " (Queue size: " + queueSize.get() + ")");
        return submission;
    }
    public boolean contains(Long submissionId) {
        return futures.containsKey(submissionId.toString());
    }

    public void completeSubmission(Long submissionId, SubmissionJob result) {
        CompletableFuture<SubmissionJob> future = futures.remove(submissionId.toString());
        if (future != null) {
            future.complete(result);
            LOG.info("✅ Completed submission: " + submissionId + " (Queue size: " + queueSize.get() + ")");
        } else {
            LOG.warn("⚠️ Submission not found for completion: " + submissionId);
        }
    }
    public int getQueueSize() {
        return queueSize.get();
    }

    public long getTotalProcessed() {
        return futures.values().stream()
                .filter(CompletableFuture::isDone)
                .count();
    }

    public int getAvailableSlots() {
        return pendingQueue.remainingCapacity();
    }
}
