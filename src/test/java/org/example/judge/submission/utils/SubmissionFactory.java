package org.example.judge.submission.utils;

import org.example.judge.submission.model.SubmissionEntity;
import org.example.judge.submission.model.dto.ProblemSubmissionReq;
import org.example.judge.submission.model.dto.SubmissionReq;

import java.util.List;
import java.util.Random;

public class SubmissionFactory {

    private static final Random RANDOM = new Random();

    public static SubmissionReq createSubmissionReq(){
        Long userId = RANDOM.nextLong();
        Long examId = RANDOM.nextLong();
        ProblemSubmissionReq problem1 = ProblemSubmissionFactory.createProblemSubmissionReq();
        ProblemSubmissionReq problem2 = ProblemSubmissionFactory.createProblemSubmissionReq();
        return new SubmissionReq(userId, examId, List.of(problem1, problem2));
    }

    public static SubmissionReq createSubmissionReq_withEmptyProblems() {
        Long userId = RANDOM.nextLong();
        Long examId = RANDOM.nextLong();
        return new SubmissionReq(userId, examId, List.of());
    }

    public static SubmissionReq createSubmissionReq_withProblemsNoLanguage() {
        Long userId = RANDOM.nextLong();
        Long examId = RANDOM.nextLong();
        ProblemSubmissionReq problem1 = ProblemSubmissionFactory.createProblemSubmissionReq_withNoLanguage();
        ProblemSubmissionReq problem2 = ProblemSubmissionFactory.createProblemSubmissionReq_withNoLanguage();
        return new SubmissionReq(userId, examId, List.of(problem1, problem2));
    }

    public static SubmissionReq createSubmissionReq_withProblemsNoSource() {
        Long userId = RANDOM.nextLong();
        Long examId = RANDOM.nextLong();
        ProblemSubmissionReq problem1 = ProblemSubmissionFactory.createProblemSubmissionReq_withNoSource();
        ProblemSubmissionReq problem2 = ProblemSubmissionFactory.createProblemSubmissionReq_withNoSource();
        return new SubmissionReq(userId, examId, List.of(problem1, problem2));
    }

    public static SubmissionReq createSubmissionReq_withProblemsHaveNoProblemId() {
        Long userId = RANDOM.nextLong();
        Long examId = RANDOM.nextLong();
        ProblemSubmissionReq problem1 = ProblemSubmissionFactory.createProblemSubmissionReq_withNoProblemId();
        ProblemSubmissionReq problem2 = ProblemSubmissionFactory.createProblemSubmissionReq_withNoProblemId();
        return new SubmissionReq(userId, examId, List.of(problem1, problem2));
    }

    public static SubmissionReq createSubmissionReq_withProblemsHaveInvalidLanguage() {
        Long userId = RANDOM.nextLong();
        Long examId = RANDOM.nextLong();
        ProblemSubmissionReq problem1 = ProblemSubmissionFactory.createProblemSubmissionReq_withInvalidLanguage();
        ProblemSubmissionReq problem2 = ProblemSubmissionFactory.createProblemSubmissionReq_withInvalidLanguage();
        return new SubmissionReq(userId, examId, List.of(problem1, problem2));
    }
}
