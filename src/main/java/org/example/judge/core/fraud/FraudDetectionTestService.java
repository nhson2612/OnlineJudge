package org.example.judge.core.fraud;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FraudDetectionTestService {

    private final FraudDetector fraudDetector;

    public FraudDetectionTestService(FraudDetector fraudDetector) {
        this.fraudDetector = fraudDetector;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        this.test();
    }
    public void test(){
        String codeFile1 = "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\submissions\\4\\A1.java";
        String codeFile2 = "C:\\Users\\Administrator.DESKTOP-98TGIBL\\IdeaProjects\\Judge\\submissions\\4\\A2.java";
        if (!Files.exists(Path.of(codeFile1)) || !Files.exists(Path.of(codeFile2))) {
            System.err.println("❌ Một trong hai file không tồn tại.");
            return;
        }

        fraudDetector.execute(codeFile1,codeFile2);
    }

}