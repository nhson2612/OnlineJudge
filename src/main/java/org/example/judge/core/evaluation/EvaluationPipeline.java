package org.example.judge.core.evaluation;

import java.util.ArrayList;
import java.util.List;

public class EvaluationPipeline {
    private final List<Stage<?, ?>> stages = new ArrayList<>();

    public static EvaluationPipeline start() {
        return new EvaluationPipeline();
    }

    public <I, O> EvaluationPipeline then(Stage<I, O> stage) {
        stages.add(stage);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <O> O execute(Object input) throws InterruptedException {
        Object current = input;
        for (Stage<?, ?> stage : stages) {
            current = ((Stage<Object, Object>) stage).process(current);
        }
        return (O) current;
    }
}

