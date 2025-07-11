package org.example.judge.core.submission;

import org.example.judge.core.domain.Testcase;
import org.example.judge.core.domain.ProblemSubmission;
import org.example.judge.core.domain.SubmissionJob;
import org.example.judge.core.domain.SubmissionStatus;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubmissionWorkerStartup {

    private final SubmissionWorker submissionWorker;

    public SubmissionWorkerStartup(SubmissionWorker submissionWorker) {
        this.submissionWorker = submissionWorker;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        submissionWorker.start();
        this.test();
    }

    private void test(){
        ProblemSubmission problemSubmission = new ProblemSubmission(
            1L,
            "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\submissions\\1\\main.cpp",
            "cpp",
            null,
            "",
            null
        );
        Testcase testcase = new Testcase(
                "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\input\\1\\1.in",
                "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\expected-output\\1\\1.out",
                100000,
                1000000,
                1
        );
        Testcase testcase2 = new Testcase(
                "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\input\\1\\2.in",
                "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\expected-output\\1\\2.out",
                100000,
                1000000,
                2
        );
        problemSubmission.setTestCases(List.of(testcase, testcase2));

        ProblemSubmission problemSubmission2 = new ProblemSubmission(
            2L,
            "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\submissions\\2\\main.cpp",
            "cpp",
            null,
            "",
            null
        );
        Testcase testcase3 = new Testcase(
                "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\input\\2\\1.in",
                "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\expected-output\\2\\1.out",
                100000,
                1000000,
                1
        );
        Testcase testcase4 = new Testcase(
                "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\input\\2\\2.in",
                "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\expected-output\\2\\2.out",
                100000,
                1000000,
                2
        );
        problemSubmission2.setTestCases(List.of(testcase3, testcase4));

        SubmissionJob submissionJob = new SubmissionJob(
            "1",
            List.of(problemSubmission, problemSubmission2),
            SubmissionStatus.PENDING,
            "0/0"
        );
        submissionWorker.submitAsync(submissionJob);
    }
}