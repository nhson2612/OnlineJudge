package org.example.judge.submission.model.dto;

import java.util.List;

public record SubmissionReq(Long userId, Long examId, List<ProblemSubmissionReq> problems) {

    public SubmissionReq {
        if (userId == null || examId == null ) {
            throw new IllegalArgumentException("Invalid submission request: userId, examId");
        }
    }
}
