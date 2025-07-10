package org.example.judge.core.configuration;

import org.example.judge.core.submission.SubmissionEvaluator;
import org.example.judge.core.submission.SubmissionWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JudgeConfiguration {
    private static final int MAX_CONCURRENT_SUBMISSIONS = 10;
    private static final int MAX_SUBMISSION_QUEUE_SIZE = 100;
    @Bean
    public SubmissionEvaluator evaluator(){
        return new SubmissionEvaluator();
    }
    @Bean
    public SubmissionWorker submissionWorker(SubmissionEvaluator evaluator){
        return new SubmissionWorker(MAX_CONCURRENT_SUBMISSIONS,MAX_SUBMISSION_QUEUE_SIZE,evaluator);
    }
}