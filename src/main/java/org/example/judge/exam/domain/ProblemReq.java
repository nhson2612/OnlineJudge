package org.example.judge.exam.domain;

import org.example.judge.problem.model.Topic;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProblemReq(Long id, String name, String description, String inputFormat, String outputFormat,
                         int timeLimit, int memoryLimit, List<Long> publicTestCaseIds, boolean isFunctionOnly, List<TopicReq> topics, String mainTemplate, Long createBy, MultipartFile input, MultipartFile expectedOutput) {
    public ProblemReq {
        if(name==null || name.isEmpty()){
            throw new IllegalArgumentException("Problem name cannot be null or empty");
        }
    }
}
