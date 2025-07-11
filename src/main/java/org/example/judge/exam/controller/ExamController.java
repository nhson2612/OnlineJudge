package org.example.judge.exam.controller;

import org.example.judge.exam.domain.DraftExamReq;
import org.example.judge.exam.domain.Exam;
import org.example.judge.exam.domain.ExamResponse;
import org.example.judge.exam.domain.StudentExamView;
import org.example.judge.exam.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exam")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/for-student/{studentId}")
    public ResponseEntity<List<StudentExamView>> getExamsForStudent(Long studentId) {
        List<StudentExamView> exams = examService.getAvailableExamsForStudent(studentId);
        return ResponseEntity.ok(exams);
    }

    @PostMapping("/draft")
    public ResponseEntity<Exam> saveDraftExam(DraftExamReq draftExamReq, Authentication authentication) {
        Exam savedExam = examService.saveDraftExam(draftExamReq, authentication);
        return ResponseEntity.ok(savedExam);
    }

    @GetMapping("/draft/{id}")
    public ResponseEntity<ExamResponse> getDraftExam(Long id, Authentication authentication) {
        ExamResponse exam = examService.getDraftExamById(id, authentication);
        return ResponseEntity.ok(exam);
    }

    @GetMapping("/draft")
    public ResponseEntity<List<ExamResponse>> getAllDraftExams(Authentication authentication) {
        List<ExamResponse> exams = examService.getDraftExams(authentication);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/take/{examId}")
    public ResponseEntity<ExamResponse> takeExam(Long examId, Authentication authentication) {
        ExamResponse exam = examService.takeExam(examId, authentication);
        return ResponseEntity.ok(exam);
    }
}
