package org.example.judge.exam.domain;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public record DraftExamReq(Long id,String name, Instant startTime, Instant endTime, List<Long> classRoomIds, Set<ProblemReq> problemReqs) {

    public DraftExamReq {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Exam name cannot be null or blank");
        }
    }
}
