//package org.example.judge.core;
//
//import org.example.judge.submission.model.old.JudgeResult;
//import org.example.judge.submission.model.old.Submission;
//import org.example.judge.submission.model.SubmissionStatus;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.atomic.AtomicLong;
//
//public class JudgeQueueManager {
//    private final BlockingQueue<Submission> pendingQueue;
//    private final ConcurrentHashMap<String, Submission> submissions;
//    private final ConcurrentHashMap<String, CompletableFuture<JudgeResult>> futures;
//    private final AtomicInteger queueSize;
//    private final AtomicLong totalProcessed;
//
//    public JudgeQueueManager(int maxQueueSize) {
//        this.pendingQueue = new LinkedBlockingQueue<>(maxQueueSize);
//        this.submissions = new ConcurrentHashMap<>();
//        this.futures = new ConcurrentHashMap<>();
//        this.queueSize = new AtomicInteger(0);
//        this.totalProcessed = new AtomicLong(0);
//    }
//
//    public CompletableFuture<JudgeResult> submitAsync(Submission submission) {
//        CompletableFuture<JudgeResult> future = new CompletableFuture<>();
//
//        try {
//            if (submissions.containsKey(submission.getId())) {
//                future.completeExceptionally(new IllegalArgumentException("Submission already exists: " + submission.getId()));
//                return future;
//            }
//            if (pendingQueue.offer(submission)) {
//                submissions.put(submission.getId(), submission);
//                futures.put(submission.getId(), future);
//                queueSize.incrementAndGet();
//
//                System.out.println("📥 Queued submission: " + submission.getId() + " (Queue size: " + queueSize.get() + ")");
//            } else {
//                future.completeExceptionally(new IllegalStateException("Queue is full"));
//            }
//
//        } catch (Exception e) {
//            future.completeExceptionally(e);
//        }
//
//        return future;
//    }
//
//    public Submission takeSubmission() throws InterruptedException {
//        Submission submission = pendingQueue.take();
//        queueSize.decrementAndGet();
//        return submission;
//    }
//
//    public void completeSubmission(String submissionId, JudgeResult result) {
//        Submission submission = submissions.get(submissionId);
//        if (submission != null) {
//            submission.setResult(result);
//            submission.setStatus(result.getStatus());
//
//            CompletableFuture<JudgeResult> future = futures.remove(submissionId);
//            if (future != null) {
//                future.complete(result);
//            }
//
//            totalProcessed.incrementAndGet();
//            System.out.println("✅ Completed submission: " + submissionId);
//        }
//    }
//
//    public void failSubmission(String submissionId, Exception error) {
//        Submission submission = submissions.get(submissionId);
//        if (submission != null) {
//            submission.setStatus(SubmissionStatus.FAILED);
//
//            CompletableFuture<JudgeResult> future = futures.remove(submissionId);
//            if (future != null) {
//                future.completeExceptionally(error);
//            }
//
//            System.out.println("❌ Failed submission: " + submissionId + " - " + error.getMessage());
//        }
//    }
//
//    public Submission getSubmission(String submissionId) {
//        return submissions.get(submissionId);
//    }
//
//    public int getQueueSize() {
//        return queueSize.get();
//    }
//
//    public long getTotalProcessed() {
//        return totalProcessed.get();
//    }
//
//    public Set<String> getAllSubmissionIds() {
//        return new HashSet<>(submissions.keySet());
//    }
//}