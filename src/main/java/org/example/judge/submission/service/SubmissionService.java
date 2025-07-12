package org.example.judge.submission.service;

import org.example.judge.FileUtils;
import org.example.judge.submission.model.ProblemSubmissionEntity;
import org.example.judge.submission.model.SubmissionEntity;
import org.example.judge.submission.model.dto.ProblemSubmissionReq;
import org.example.judge.submission.model.dto.SubmissionReq;
import org.example.judge.submission.repository.SubmissionEntityRepository;
import org.springframework.stereotype.Service;

@Service
public class SubmissionService {
    private final SubmissionEntityRepository submissionEntityRepository;
    private static final String CODE_PROBLEM_SUBMISSION_PATH =
            "/submissions/<submissionId>/<problemSubmissionId>/";

    public SubmissionService(SubmissionEntityRepository submissionEntityRepository) {
        this.submissionEntityRepository = submissionEntityRepository;
    }

    public SubmissionEntity submit(SubmissionReq req) {
        SubmissionEntity submissionEntity = new SubmissionEntity(req);
        // Step 1: Save lần đầu để sinh submissionId + problemSubmissionId
        SubmissionEntity saved = submissionEntityRepository.save(submissionEntity);
        // Step 2: Gán sourceCodePath cho từng problem
        for (int i = 0; i < req.problems().size(); i++) {
            ProblemSubmissionReq problemReq = req.problems().get(i);
            ProblemSubmissionEntity problem = saved.getProblems().get(i);
            String nameFile = nameFileGenerator(problem.getLanguage());
            String path = CODE_PROBLEM_SUBMISSION_PATH
                    .replace("<submissionId>", saved.getSubmissionId().toString())
                    .replace("<problemSubmissionId>", problem.getId().toString())
                    + nameFile;
            problem.setSourceCodePath(path);
            FileUtils.save(problemReq.source(),"submissions",nameFile, String.valueOf(saved.getSubmissionId()),String.valueOf(problem.getId()));
        }
        // Step 3: Save lại để cập nhật sourceCodePath
        return submissionEntityRepository.save(saved);
    }


    private String nameFileGenerator(String language){
        switch (language){
            case "java":
                return "Main.java";
            case "python":
                return "main.py";
            case "c":
                return "main.c";
            case "cpp":
                return "main.cpp";
            case "javascript":
                return "main.js";
            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }
    }

}
