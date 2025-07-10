package org.example.judge.core.fraud.processor.parser;

import java.util.List;

public interface FunctionParser {
    List<FunctionBlock> extractFunctions(String code);
}