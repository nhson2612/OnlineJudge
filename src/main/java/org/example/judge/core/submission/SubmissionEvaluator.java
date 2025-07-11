package org.example.judge.core.submission;

import org.example.judge.core.domain.ExecutionContext;
import org.example.judge.core.domain.TestCaseResult;
import org.example.judge.core.evaluation.CompareStage;
import org.example.judge.core.evaluation.EvaluationPipeline;
import org.example.judge.core.evaluation.RunStage;
import org.example.judge.core.evaluation.ValidStage;
import org.example.judge.core.domain.ProblemSubmission;
import org.example.judge.core.domain.SubmissionJob;
import org.example.judge.core.domain.SubmissionStatus;

import java.util.ArrayList;
import java.util.List;

public class SubmissionEvaluator {

    public SubmissionJob execute(SubmissionJob submissionJob){
        List<ProblemSubmission> problems = submissionJob.getProblems();
        problems.stream().forEach(problem -> {
            String sourceCodePath = problem.getSourceCodePath();
            String language = problem.getLanguage();
            List<TestCaseResult> problemTestCasesResults = new ArrayList<>();
            problem.getTestCases().stream().forEach(testcase -> {
                ExecutionContext context = new ExecutionContext(testcase, sourceCodePath);
                EvaluationPipeline pipeline = buildPipeline(language);
                TestCaseResult result = null;
                try {
                    result = pipeline.execute(context);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                problemTestCasesResults.add(result);
            });
            problem.setResult(problemTestCasesResults);
        });

        boolean atLeastOnePassed = submissionJob.getProblems().stream()
                        .anyMatch(ProblemSubmission::isPassed);
        submissionJob.setOverallStatus(
                atLeastOnePassed ? SubmissionStatus.COMPLETED : SubmissionStatus.FAILED
        );

        long passedCount = submissionJob.getProblems().stream()
                .filter(ProblemSubmission::isPassed)
                .count();
        submissionJob.setOverallScore(passedCount);
        return submissionJob;
    }

    private EvaluationPipeline buildPipeline(String language) {
        return EvaluationPipeline.start()
                .then(new ValidStage())
                .then(new RunStage(language))
                .then(new CompareStage());
    }
}
