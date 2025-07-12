package org.example.judge.submission.model.dto;

public record ProblemSubmissionReq(String source,
                                   String language,
                                   Long problemId) {
    public ProblemSubmissionReq {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("Source code path cannot be null or blank");
        }
        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException("Language cannot be null or blank");
        }
        if (problemId == null) {
            throw new IllegalArgumentException("Problem ID cannot be null");
        }
    }
}
