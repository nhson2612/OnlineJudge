package org.example.judge.core.submission;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SubmissionFileManager {

    private static volatile SubmissionFileManager INSTANCE;
    private final Path workdir = Paths.get("submissions");

    public static SubmissionFileManager getInstance() {
        if (INSTANCE == null) {
            synchronized (SubmissionFileManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SubmissionFileManager();
                }
            }
        }
        return INSTANCE;
    }

    public Map<String, Path> saveSourceCode(String submissionId, String sourceCode, String language) throws IOException {
        Path submissionDir = workdir.resolve(submissionId);
        Files.createDirectories(submissionDir);

        String sourceFileName = getSourceFileName(language);
        Path sourceFile = submissionDir.resolve(sourceFileName);
        Files.writeString(sourceFile, sourceCode);

        Map<String, Path> filePaths = new HashMap<>();
        filePaths.put("source", sourceFile);
        filePaths.put("dir", submissionDir);
        return filePaths;
    }

    public void deleteSubmissionFiles(String submissionId) {
        Path submissionDir = workdir.resolve(submissionId);
        try {
            if (Files.exists(submissionDir)) {
                Files.walk(submissionDir)
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException ignored) {
                            }
                        });
            }
        } catch (IOException e) {
            System.err.println("⚠️ Failed to delete submission dir: " + submissionDir);
        }
    }

    public Path getSubmissionDir(String submissionId) {
        return workdir.resolve(submissionId);
    }

    public String getSourceFileName(String language) {
        switch (language.toLowerCase()) {
            case "cpp":
                return "main.cpp";
            case "java":
                return "Main.java";
            case "python":
                return "main.py";
            default:
                return "main.txt";
        }
    }
}
