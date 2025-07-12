package org.example.judge;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class FileUtils {

    private static final Map<String, Path> fileLocations = Map.of(
            "draft/input", Paths.get("draft/input"),
            "draft/expectedOutput", Paths.get("draft/expectedOutput"),
            "input",Paths.get("input"),
            "expected-output", Paths.get("expected-output"),
            "submissions",Paths.get("submissions"));

    public static Path getFilePath(String dir, String... subPaths) {
        Path baseDir = fileLocations.get(dir);
        if (baseDir == null) {
            throw new IllegalArgumentException("Unknown directory: " + dir);
        }
        Path fullPath = baseDir;
        for (String subPath : subPaths) {
            fullPath = fullPath.resolve(subPath);
        }

        return fullPath;
    }

    public static Path save(MultipartFile multipartFile, String dir) {
        try {
            Path filePath = fileLocations.get(dir).resolve(multipartFile.getOriginalFilename());
            multipartFile.transferTo(filePath);
            return filePath;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }
    }

    public static Path save(String content, String dir, String fileName,String... subPaths) {
        try {
            Path baseDir = fileLocations.get(dir);
            if (baseDir == null) {
                throw new IllegalArgumentException("Unknown base dir key: " + dir);
            }
            // Tạo path đầy đủ
            Path targetPath = baseDir;
            for (String part : subPaths) {
                targetPath = targetPath.resolve(part);
            }
            targetPath = targetPath.resolve(fileName);

            // Tạo thư mục cha nếu chưa có
            Files.createDirectories(targetPath.getParent());

            // Ghi nội dung
            Files.writeString(targetPath, content);

            return targetPath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }
    }
    public static Path save(MultipartFile multipartFile, String dir, String fileName) {
        try {
            Path baseDir = fileLocations.get(dir);
            Files.createDirectories(baseDir);
            Path filePath = baseDir.resolve(fileName);
            multipartFile.transferTo(filePath);
            return filePath;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }
    }
    public static void remove(Path filePath) {
        try {
            if(filePath != null && filePath.toFile().exists()) {
                java.nio.file.Files.delete(filePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage(), e);
        }
    }

    public static void remove(String dir, String fileName) {
        Path filePath = fileLocations.get(dir).resolve(fileName);
        remove(filePath);
    }

    public static String read(String dir, String... subPaths) {
        Path basePath = fileLocations.get(dir);
        if (basePath == null) {
            throw new IllegalArgumentException("Unknown base directory: " + dir);
        }

        Path fullPath = basePath;
        for (String subPath : subPaths) {
            fullPath = fullPath.resolve(subPath);
        }

        try {
            return java.nio.file.Files.readString(fullPath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + fullPath + " - " + e.getMessage(), e);
        }
    }
    public static int countFiles(String dir, String subDir) {
        Path baseDir = fileLocations.get(dir);
        if (baseDir == null) {
            throw new IllegalArgumentException("Unknown directory: " + dir);
        }

        Path fullPath = baseDir.resolve(subDir);
        if (!Files.exists(fullPath) || !Files.isDirectory(fullPath)) {
            return 0;
        }

        try (Stream<Path> files = Files.list(fullPath)) {
            return (int) files.filter(Files::isRegularFile).count();
        } catch (IOException e) {
            throw new RuntimeException("Failed to count files in " + fullPath, e);
        }
    }
    public static Map<String, List<Path>> splitAndSaveTestCases(Long problemId, Path inputPath, Path outputPath) {
        try {
            String inputContent = Files.readString(inputPath);
            String outputContent = Files.readString(outputPath);

            // Tách theo delimiter '---'
            String[] inputCases = inputContent.split("---\\R?");
            String[] outputCases = outputContent.split("---\\R?");

            if (inputCases.length != outputCases.length) {
                throw new IllegalArgumentException("Số lượng input và output test case không khớp");
            }

            // Tạo thư mục nếu chưa có
            Path inputDir = fileLocations.get("input").resolve(problemId.toString());
            Path outputDir = fileLocations.get("expected-output").resolve(problemId.toString());

            Files.createDirectories(inputDir);
            Files.createDirectories(outputDir);

            List<Path> inputPaths = new java.util.ArrayList<>();
            List<Path> outputPaths = new java.util.ArrayList<>();

            for (int i = 0; i < inputCases.length; i++) {
                String inName = (i + 1) + ".in";
                String outName = (i + 1) + ".out";

                Path inPath = inputDir.resolve(inName);
                Path outPath = outputDir.resolve(outName);

                Files.writeString(inPath, inputCases[i].trim());
                Files.writeString(outPath, outputCases[i].trim());

                inputPaths.add(inPath);
                outputPaths.add(outPath);
            }

            return Map.of(
                    "inputs", inputPaths,
                    "outputs", outputPaths
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to split and save test cases: " + e.getMessage(), e);
        }
    }

}