package org.example.judge.core.fraud;

import org.example.judge.core.fraud.domain.LanguageType;
import org.example.judge.core.fraud.processor.parser.FunctionParser;
import org.example.judge.core.fraud.processor.parser.JavaFunctionParser;
import org.example.judge.core.fraud.processor.parser.PythonFunctionParser;
import org.example.judge.core.fraud.tokenizer.JavaTokenizer;
import org.example.judge.core.fraud.tokenizer.PythonTokenizer;
import org.example.judge.core.fraud.tokenizer.Tokenizer;

public class LanguageDetector {

    public static FunctionParser getParserFor(LanguageType lang) {
        return switch (lang) {
            case JAVA -> new JavaFunctionParser();
            case PYTHON -> new PythonFunctionParser();
            default -> throw new IllegalArgumentException("Unsupported or unknown language");
        };
    }

    public static Tokenizer getTokenizerFor(LanguageType lang) {
        return switch (lang) {
            case JAVA -> new JavaTokenizer();
            case PYTHON -> new PythonTokenizer();
            default -> throw new IllegalArgumentException("Unsupported or unknown language");
        };
    }
}

