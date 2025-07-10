package org.example.judge.core.fraud.tokenizer;

import java.util.List;

public interface Tokenizer {
    List<String> extractTokens(String code);
}