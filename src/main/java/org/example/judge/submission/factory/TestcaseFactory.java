package org.example.judge.submission.factory;

import org.example.judge.FileUtils;
import org.example.judge.core.domain.Testcase;

public class TestcaseFactory {
    public static Testcase create(Long problemId, int orderIndex) {
        String inputPath = FileUtils.getFilePath("input",String.valueOf(problemId), orderIndex + ".in").toString();
        String expectedOutputPath = FileUtils.getFilePath("expected-output", String.valueOf(problemId), orderIndex + ".out").toString();
        long timeLimit = 100000; // Giới hạn thời gian mặc định (ms)
        long memoryLimit = 1000000; // Giới hạn bộ nhớ mặc định (bytes)
        return new Testcase(
            inputPath,
            expectedOutputPath,
            timeLimit,
            memoryLimit,
            orderIndex
        );
    }
}
