package org.example.judge.core.fraud.processor;

import org.example.judge.core.fraud.domain.DetectionContext;

public class CodeNormalizeFraudStage implements FraudStage<DetectionContext,DetectionContext> {

    @Override
    public DetectionContext process(DetectionContext input) {
        String code = input.getCodeWithoutComments();

        String[] lines = code.split("\\R");
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                String singleSpaced = trimmed.replaceAll("\\s+", " ");
                sb.append(singleSpaced).append("\n");
            }
        }

        input.setNormalizedCode(sb.toString().trim());
        return input;
    }

}
