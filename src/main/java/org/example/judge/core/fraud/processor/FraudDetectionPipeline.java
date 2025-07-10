package org.example.judge.core.fraud.processor;

import org.example.judge.core.fraud.domain.DetectionContext;

import java.util.ArrayList;
import java.util.List;

public class FraudDetectionPipeline {
    private final List<FraudStage<DetectionContext,DetectionContext>> stages = new ArrayList<>();

    public static FraudDetectionPipeline start() {
        return new FraudDetectionPipeline();
    }

    public <I, O> FraudDetectionPipeline then(FraudStage<I, O> stage) {
        stages.add((FraudStage<DetectionContext, DetectionContext>) stage);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <O> O execute(DetectionContext input) throws InterruptedException {
        Object current = input;
        for (FraudStage<?, ?> stage : stages) {
            current = ((FraudStage<Object, Object>) stage).process(current);
        }
        return (O) current;
    }
}
