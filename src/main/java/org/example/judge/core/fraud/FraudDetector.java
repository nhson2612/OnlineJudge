package org.example.judge.core.fraud;

import org.example.judge.core.fraud.compare.TokenComparator;
import org.example.judge.core.fraud.domain.DetectionContext;
import org.example.judge.core.fraud.domain.LanguageType;
import org.example.judge.core.fraud.processor.*;
import org.example.judge.core.fraud.processor.parser.FunctionParser;
import org.example.judge.core.fraud.tokenizer.Tokenizer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class FraudDetector {
    private final TokenComparator tokenComparator;

    public FraudDetector(TokenComparator tokenComparator) {
        this.tokenComparator = tokenComparator;
    }

    public void execute(String codePath1, String codePath2) {
        LanguageType lang1 = this.determineLanguageType(codePath1);
        LanguageType lang2 = this.determineLanguageType(codePath2);

        if(!lang1.equals(lang2)) return;

        if(lang1 == LanguageType.UNKNOWN || lang2 == LanguageType.UNKNOWN) {
            System.err.println("⚠️ Không nhận diện được ngôn ngữ của mã nguồn.");
            return;
        }
        FunctionParser parser = LanguageDetector.getParserFor(lang1);
        FraudDetectionPipeline pipeline = this.buildPipeline(parser);
        try {
            String code1 = Files.readString(Path.of(codePath1));
            String code2 = Files.readString(Path.of(codePath2));
            DetectionContext context1 = new DetectionContext(codePath1,code1);
            DetectionContext context2 = new DetectionContext(codePath2,code2);
            pipeline.execute(context1);
            pipeline.execute(context2);

            Tokenizer tokenizer1 = LanguageDetector.getTokenizerFor(lang1);
            Tokenizer tokenizer2 = LanguageDetector.getTokenizerFor(lang2);
            String codeToTokenize1 = context1.getReorderedCode();
            if (codeToTokenize1 == null || codeToTokenize1.isBlank()) {
                codeToTokenize1 = context1.getNormalizedCode();
            }
            List<String> tokens1 = tokenizer1.extractTokens(codeToTokenize1);
            String codeToTokenize2 = context2.getReorderedCode();
            if (codeToTokenize2 == null || codeToTokenize2.isBlank()) {
                codeToTokenize2 = context2.getNormalizedCode();
            }
            List<String> tokens2 = tokenizer2.extractTokens(codeToTokenize2);
            context1.setTokens(tokens1);
            context2.setTokens(tokens2);

            Map<String, Double> result = tokenComparator.compareAll(context1, List.of(context2));
            result.forEach((path, score) -> {
                System.out.printf("So sánh %s với %s: %.2f%n", context1.getSourceCodePath(), path,score);
                if (tokenComparator.isPotentialFraud(score)) {
                    System.out.println("⚠️  Khả năng gian lận cao!");
                }else {
                    System.out.println("✅  Không có dấu hiệu gian lận.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private FraudDetectionPipeline buildPipeline(FunctionParser parser) {
        return new FraudDetectionPipeline()
                .then(new CommentRemoveStage())        // Bỏ comment trước
                .then(new CodeNormalizeFraudStage())   // Chuẩn hoá sau
                .then(new FunctionReorderStage(parser));

    }

    private LanguageType determineLanguageType(String codePath) {
        if (codePath.endsWith(".java")) return LanguageType.JAVA;
        if (codePath.endsWith(".py")) return LanguageType.PYTHON;
        return LanguageType.UNKNOWN;
    }
}