package org.example.judge.submission.service;

import jakarta.transaction.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.judge.core.domain.SubmissionStatus;
import org.example.judge.core.submission.SubmissionWorker;
import org.example.judge.submission.model.ProblemSubmissionEntity;
import org.example.judge.submission.model.SubmissionEntity;
import org.example.judge.submission.model.dto.ProblemSubmissionReq;
import org.example.judge.submission.model.dto.SubmissionReq;
import org.example.judge.submission.repository.SubmissionEntityRepository;
import org.example.judge.submission.schedule.SubmissionScheduler;
import org.example.judge.submission.utils.SubmissionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SubmissionServiceTest {

    private static final Log log = LogFactory.getLog(SubmissionServiceTest.class);
    @Autowired
    private SubmissionWorker submissionWorker;

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private SubmissionEntityRepository submissionRepository;


    private SubmissionScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new SubmissionScheduler(submissionWorker, submissionRepository);
        submissionWorker.start();
    }

    @AfterEach
    void tearDown() {
        submissionWorker.stop();
        submissionRepository.deleteAll();
    }

    @Test
    void createSubmissionEntity_fromSubmissionReq() {
        SubmissionReq submissionReq = SubmissionFactory.createSubmissionReq(); // tạo submissionReq mock
        SubmissionEntity entity = submissionService.submit(submissionReq);     // gọi hàm submit

        assertCreateSubmissionEntity_fromSubmissionReq(entity, submissionReq); // kiểm tra kết quả
    }

    @Test
    void createSubmissionEntity_fromSubmissionReq_withEmptyProblems() {
        SubmissionReq submissionReq = SubmissionFactory.createSubmissionReq_withEmptyProblems();
        SubmissionEntity entity = submissionService.submit(submissionReq);

        assertEquals(entity.getOverallScore(),"0");
        assertEquals(entity.getOverallStatus(), SubmissionStatus.FAILED);
    }

    @Test
    void createSubmissionEntity_fromSubmissionReq_whenProblemsHaveNoLanguage() {
        assertThrows(IllegalArgumentException.class, () -> {
            SubmissionReq submissionReq = SubmissionFactory.createSubmissionReq_withProblemsNoLanguage();
            SubmissionEntity entity = submissionService.submit(submissionReq);
        });
    }

    @Test
    void createSubmissionEntity_fromSubmissionReq_whenProblemsHaveNoSource() {
        assertThrows(IllegalArgumentException.class, () -> {
            SubmissionReq submissionReq = SubmissionFactory.createSubmissionReq_withProblemsNoSource();
            SubmissionEntity entity = submissionService.submit(submissionReq);
        });
    }

    @Test
    void createSubmissionEntity_fromSubmissionReq_withProblemsHaveNoProblemId() {
        assertThrows(IllegalArgumentException.class, () -> {
            SubmissionReq submissionReq = SubmissionFactory.createSubmissionReq_withProblemsHaveNoProblemId();
            SubmissionEntity entity = submissionService.submit(submissionReq);
        });
    }

    @Test
    void createSubmissionEntity_fromSubmissionReq_withProblemsHaveInvalidLanguage() {
        assertThrows(IllegalArgumentException.class, () -> {
            SubmissionReq submissionReq = SubmissionFactory.createSubmissionReq_withProblemsHaveInvalidLanguage();
            SubmissionEntity entity = submissionService.submit(submissionReq);
        });
    }


    @Test
    void createSubmissionEntity_fromSubmissionReq_thenExecuteSubmissionScheduler() {
        SubmissionReq submissionReq = SubmissionFactory.createSubmissionReq();
        SubmissionEntity entity = submissionService.submit(submissionReq);

        scheduler.fetchAndSubmitPendingSubmissions();

        Optional<SubmissionEntity> byId = submissionRepository.findById(entity.getSubmissionId());
        assertTrue(byId.isPresent());
        entity = byId.get();
        log.info(entity);

        assertNotNull(entity.getOverallStatus());
        assertNotNull(entity.getOverallScore());

        SubmissionEntity finalEntity = null;
        int retry = 30;
        while (retry-- > 0) {
            Optional<SubmissionEntity> byId1 = submissionRepository.findById(entity.getSubmissionId());
            if (byId1.isPresent() && SubmissionStatus.COMPLETED.equals(byId1.get().getOverallStatus())) {
                finalEntity = byId1.get();
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Thread was interrupted while waiting for submission to complete.");
            }
        }

        assertNotNull(finalEntity, "Submission was not completed in 30 seconds.");
        assertNotNull(finalEntity.getOverallScore());
        assertEquals(SubmissionStatus.COMPLETED, finalEntity.getOverallStatus());
    }



    private void assertCreateSubmissionEntity_fromSubmissionReq(SubmissionEntity entity, SubmissionReq submissionReq) {
        assertNotNull(entity);
        assertNotNull(entity.getSubmissionId());
        assertEquals(submissionReq.userId(), entity.getUserId());
        assertEquals(submissionReq.problems().size(), entity.getProblems().size());

        for (int i = 0; i < entity.getProblems().size(); i++) {
            ProblemSubmissionEntity problem = entity.getProblems().get(i);
            ProblemSubmissionReq req = submissionReq.problems().get(i);

            assertNotNull(problem.getId());
            assertEquals(req.problemId(), problem.getProblemId());
            assertEquals(req.language(), problem.getLanguage());
            assertEquals(0, problem.getResults().size());
            assertNull(problem.getScore());
            assertEquals(problem.getTestcaseSet().size(),0);

            // Kiểm tra sourceCodePath
            String expectedFileName = nameFileGenerator(req.language()); // giống service
            String expectedPath = String.format("/submissions/%d/%d/%s",
                    entity.getSubmissionId(), problem.getId(), expectedFileName);

            assertEquals(expectedPath, problem.getSourceCodePath());
        }
    }
    private String nameFileGenerator(String language){
        return switch (language.toLowerCase()) {
            case "java" -> "Main.java";
            case "python" -> "main.py";
            case "c" -> "main.c";
            case "cpp" -> "main.cpp";
            case "javascript" -> "main.js";
            default -> throw new IllegalArgumentException("Unsupported language: " + language);
        };
    }
}