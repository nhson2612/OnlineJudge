package org.example.judge.exam.checker;

import org.example.judge.exam.domain.Exam;
import org.example.judge.exam.domain.ExamStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ExamChecker {

    public boolean canTakeExam(Exam exam, Long studentId) {
        if (exam.isDraft() || !exam.getUpdateBy().equals(studentId)) {
            throw new IllegalArgumentException("Exam is not draft or update by");
        }
        if (exam.getStatus() != ExamStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Exam is not in progress");
        }
        boolean isAllowed = exam.getClassRooms().stream()
                .anyMatch(classRoom -> classRoom.getStudents().stream()
                        .anyMatch(student -> student.getId().equals(studentId)));
        if (!isAllowed) {
            throw new IllegalArgumentException("Student is not allowed to take this exam");
        }
        if(checkOverDue(exam)) {
            exam.setStatus(ExamStatus.COMPLETED);
            throw new IllegalArgumentException("Exam is overdue");
        }
        return true;
    }

    public boolean canReleaseDraft(Exam exam, Long userId) {
        if (!exam.isDraft() || !exam.getUpdateBy().equals(userId)) {
            throw new IllegalArgumentException("Exam is not draft or update by");
        }
        return true;
    }

    public boolean canStartExam(Exam exam, Long userId) {
        if (exam.isDraft() || !exam.getUpdateBy().equals(userId)) {
            throw new IllegalArgumentException("Exam is not draft or update by");
        }
        return true;
    }

    public boolean checkOverDue (Exam exam) {
        return exam.getEndTime() != null && exam.getEndTime().isBefore(Instant.now());
    }
}
