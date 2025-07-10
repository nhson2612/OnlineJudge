//package org.example.judge;
//
//import org.example.judge.core.AsyncDockerJudgeRunner;
//import org.example.judge.submission.model.old.JudgeResult;
//import org.example.judge.submission.model.old.Submission;
//
//import java.util.concurrent.CompletableFuture;
//
//public class Main {
//    public static void main(String[] args) {
//        AsyncDockerJudgeRunner judgeSystem = new AsyncDockerJudgeRunner(3, 10);
//        judgeSystem.start();
//
//        Submission s1 = new Submission("1",
//                "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\submissions\\1\\input.txt",
//                "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\submissions\\1\\input.txt",
//                "cpp");
//        Submission s2 = new Submission("2",
//                "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\submissions\\2\\input.txt",
//                "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\submissions\\2\\input.txt",
//                "cpp");
//        Submission s3 = new Submission("3",
//                "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\submissions\\3\\input.txt",
//                "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\submissions\\3\\input.txt",
//                "cpp");
//
//        // 🚀 Submit bài
//        CompletableFuture<JudgeResult> f1 = judgeSystem.submitAsync(s1.getId(), s1.getSourceCodePath(), s1.getInputDataPath(), s1.getLanguage());
//        CompletableFuture<JudgeResult> f2 = judgeSystem.submitAsync(s2.getId(), s2.getSourceCodePath(), s2.getInputDataPath(), s2.getLanguage());
//        CompletableFuture<JudgeResult> f3 = judgeSystem.submitAsync(s3.getId(), s3.getSourceCodePath(), s3.getInputDataPath(), s3.getLanguage());
//
//        f1.thenAccept(result -> System.out.println("📄 Result for " + result.getSubmissionId() + ":\n" + result));
//        f2.thenAccept(result -> System.out.println("📄 Result for " + result.getSubmissionId() + ":\n" + result));
//        f3.thenAccept(result -> System.out.println("📄 Result for " + result.getSubmissionId() + ":\n" + result));
//
//        // ⌛ Đợi tất cả hoàn thành
//        CompletableFuture.allOf(f1, f2, f3).join();
//
//        // 📊 In thống kê
//        judgeSystem.printStats();
//
//        // ⏹️ Dừng hệ thống
//        judgeSystem.stop();
//
//    }
//}
