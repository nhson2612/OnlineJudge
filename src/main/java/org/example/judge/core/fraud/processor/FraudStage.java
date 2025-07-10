package org.example.judge.core.fraud.processor;

public interface FraudStage<I,O> {
    O process(I input);
}
