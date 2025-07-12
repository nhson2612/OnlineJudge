package org.example.judge.submission.schedule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.judge.FileUtils;
import org.example.judge.core.domain.ProblemSubmission;
import org.example.judge.core.domain.SubmissionJob;
import org.example.judge.core.domain.TestCaseResult;
import org.example.judge.core.domain.Testcase;
import org.example.judge.core.factory.SubmissionJobFactory;
import org.example.judge.core.submission.SubmissionEvaluator;
import org.example.judge.core.submission.SubmissionWorker;
import org.example.judge.submission.factory.TestcaseFactory;
import org.example.judge.submission.model.ProblemSubmissionEntity;
import org.example.judge.submission.model.SubmissionEntity;
import org.example.judge.submission.model.TestCaseResultEntity;
import org.example.judge.submission.repository.SubmissionEntityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SubmissionScheduler {

    private static final Log log = LogFactory.getLog(SubmissionScheduler.class);
    private final SubmissionWorker submissionWorker;
    private final SubmissionEntityRepository submissionRepository;
    private final Map<String, SubmissionEntity> submissionContext = new ConcurrentHashMap<>();


    public SubmissionScheduler(SubmissionWorker submissionWorker, SubmissionEntityRepository submissionRepository) {
        this.submissionWorker = submissionWorker;
        this.submissionRepository = submissionRepository;
    }

    @Scheduled(fixedDelay = 3000)
    public void fetchAndSubmitPendingSubmissions() {
        int remainingCapacity = submissionWorker.getRemainingCapacity();
        List<SubmissionEntity> pendingSubs = submissionRepository.findRandomPendingSubmissions(remainingCapacity);
        if (pendingSubs.isEmpty()) {
            log.info("No pending submissions to process at this time.");
            return; // No pending submissions to process
        }
        for (SubmissionEntity submissionEntity : pendingSubs) {
            List<ProblemSubmissionEntity> problems = submissionEntity.getProblems();
            for(ProblemSubmissionEntity problem : problems) {
                int input = FileUtils.countFiles("input", String.valueOf(problem.getProblemId()));
                List<Testcase> testcases = new ArrayList<>();
                for(int i = 0; i < input; i++) {
                    testcases.add(TestcaseFactory.create(problem.getProblemId(), i));
                }
                problem.setTestcaseSet(testcases);
            }
            SubmissionJob submissionJob = SubmissionJobFactory.fromEntity(submissionEntity);
            submissionContext.put(submissionEntity.getSubmissionId().toString(), submissionEntity);
            submissionWorker.submitAsync(submissionJob);
            submissionWorker.setOnCompleteCallback(this::onComplete);
        }
    }

    private void onComplete(SubmissionJob job) {
        SubmissionEntity entity = submissionContext.remove(job.getSubmissionId());
        if (entity != null) {
            entity.setOverallStatus(job.getOverallStatus());
            entity.setOverallScore(job.getOverallScore());

            List<ProblemSubmissionEntity> problemEntities = entity.getProblems();
            List<ProblemSubmission> problemSubmissions = job.getProblems();

            for (ProblemSubmissionEntity problemEntity : problemEntities) {
                problemSubmissions.stream()
                        .filter(ps -> ps.getProblemId().equals(problemEntity.getProblemId()))
                        .findFirst()
                        .ifPresent(ps -> mapProblemToEntity(problemEntity, ps));
            }
            submissionRepository.save(entity);
        }
    }

    public ProblemSubmissionEntity mapProblemToEntity(ProblemSubmissionEntity entity, ProblemSubmission submission) {
        entity.setScore(submission.getScore());
        entity.getResults().clear();

        for (TestCaseResult result : submission.getResult()) {
            TestCaseResultEntity resultEntity = new TestCaseResultEntity();
            mapTestCaseResultToEntity(resultEntity, result);
            resultEntity.setProblemSubmission(entity);
            entity.getResults().add(resultEntity);
        }

        return entity;
    }

    public TestCaseResultEntity mapTestCaseResultToEntity(TestCaseResultEntity entity, TestCaseResult result) {
        entity.setActualOutputPath(result.getActualOutputPath());
        entity.setExpectedOutputPath(result.getExpectedOutputPath());
        entity.setMemoryUsage(result.getMemoryUsage());
        entity.setExecutionTime(result.getExecutionTime());
        entity.setType(result.getType());
        return entity;
    }
}
