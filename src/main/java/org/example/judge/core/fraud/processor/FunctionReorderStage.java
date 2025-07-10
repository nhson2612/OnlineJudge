package org.example.judge.core.fraud.processor;

import org.example.judge.core.fraud.domain.DetectionContext;
import org.example.judge.core.fraud.processor.parser.FunctionBlock;
import org.example.judge.core.fraud.processor.parser.FunctionParser;

import java.util.Comparator;
import java.util.List;

public class FunctionReorderStage implements FraudStage<DetectionContext,DetectionContext>{

    private final FunctionParser parser;

    public FunctionReorderStage(FunctionParser parser) {
        this.parser = parser;
    }

    @Override
    public DetectionContext process(DetectionContext input) {
        String code = input.getNormalizedCode();
        List<FunctionBlock> blocks = parser.extractFunctions(code);

        blocks.sort(Comparator.comparing(FunctionBlock::getName));
        StringBuilder strippedCode = new StringBuilder(code);
        for (FunctionBlock block : blocks) {
            for (int i = block.getStartPosition(); i < block.getEndPosition(); i++) {
                strippedCode.setCharAt(i, ' ');
            }
        }
        String baseCode = strippedCode.toString().trim();
        StringBuilder result = new StringBuilder(baseCode);
        for (FunctionBlock block : blocks) {
            result.append("\n\n").append(block.getContent().trim());
        }
        input.setReorderedCode(result.toString().trim());
        return input;
    }
}
