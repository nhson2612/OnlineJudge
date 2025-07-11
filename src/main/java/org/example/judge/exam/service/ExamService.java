package org.example.judge.exam.service;

import org.example.judge.FileUtils;
import org.example.judge.classroom.repostiroty.ClassRoomRepository;
import org.example.judge.exam.checker.ExamChecker;
import org.example.judge.exam.domain.*;
import org.example.judge.exam.repository.ExamRepository;
import org.example.judge.problem.model.Problem;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final ClassRoomRepository classRepository;
    private final ExamChecker examChecker;

    public ExamService(ExamRepository examRepository, ClassRoomRepository classRepository, ExamChecker examChecker) {
        this.examRepository = examRepository;
        this.classRepository = classRepository;
        this.examChecker = examChecker;
    }

    public Exam saveDraftExam(DraftExamReq draftExamReq,
                              Authentication authentication) {
        Long userId = extractUserId(authentication);
        Exam exam = new Exam(draftExamReq);
        exam.setUpdateBy(userId);

        // TODO : Remove duplicate file if necessary

        exam.getProblems().forEach(problem -> problem.setCreateBy(userId));
        Map<String, Problem> nameToProblem = exam.getProblems().stream()
                .collect(Collectors.toMap(Problem::getName, p -> p));
        for (ProblemReq problemReq : draftExamReq.problemReqs()) {
            Problem problem = nameToProblem.get(problemReq.name());
            if (problem == null) continue;
            if (problemReq.input() != null && !problemReq.input().isEmpty()) {
                String inputFileName = UUID.randomUUID() + "_" + problemReq.input().getOriginalFilename();
                FileUtils.save(problemReq.input(), "draft/input", inputFileName);
                problem.setInputFileName(inputFileName);
            }
            if (problemReq.expectedOutput() != null && !problemReq.expectedOutput().isEmpty()) {
                String expectedFileName = UUID.randomUUID() + "_" + problemReq.expectedOutput().getOriginalFilename();
                FileUtils.save(problemReq.expectedOutput(), "draft/expectedOutput", expectedFileName);
                problem.setExpectedOutputFileName(expectedFileName);
            }
        }
        return examRepository.save(exam);
    }

    public void releaseDraftExam(Long id, Authentication authentication) {
        Long userId = extractUserId(authentication);
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));
        examChecker.canReleaseDraft(exam, userId);
        exam.setDraft(false);
        exam.setUpdateBy(userId);
        exam.getProblems().forEach(problem -> {
            Path inputFilePath = FileUtils.getFilePath("draft/input", problem.getInputFileName());
            Path expectedOutputFilePath = FileUtils.getFilePath("draft/expectedOutput", problem.getExpectedOutputFileName());

            FileUtils.splitAndSaveTestCases(problem.getId(), inputFilePath, expectedOutputFilePath);
        });
        examRepository.save(exam);
    }

    public void startExam(Long id, Authentication authentication) {
        Long userId = extractUserId(authentication);
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        examChecker.canStartExam(exam,userId);

        exam.setStatus(ExamStatus.IN_PROGRESS);
        exam.setUpdateBy(userId);
        examRepository.save(exam);
    }

    public ExamResponse takeExam(Long id, Authentication authentication) {
        Long userId = extractUserId(authentication);
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));
        examChecker.canTakeExam(exam, userId);
        return examRepository.findExamResponseById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot load exam details"));
    }

    public List<ExamResponse> getDraftExams(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return examRepository.findAllByIsDraftTrueAndUpdateBy(userId);
    }

    public List<StudentExamView> getAvailableExamsForStudent(Long studentId) {
        List<Long> classIds = classRepository.findClassRoomIdsByStudentID(studentId);
        return examRepository.findAllAvailableExamsForStudent(classIds);
    }

    public ExamResponse getDraftExamById(Long id, Authentication authentication) {
        Long userId = extractUserId(authentication);
        return examRepository.findByIdAndIsDraftTrueAndUpdateBy(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Draft exam not found or access denied"));
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalArgumentException("Authentication or user ID is not available");
        }
        return Long.valueOf(authentication.getName());
    }
}