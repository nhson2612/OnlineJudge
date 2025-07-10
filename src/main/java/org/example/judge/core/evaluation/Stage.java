package org.example.judge.core.evaluation;

public interface Stage<I,O> {
    O process(I input);
}

