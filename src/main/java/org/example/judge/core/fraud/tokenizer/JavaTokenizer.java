package org.example.judge.core.fraud.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaTokenizer implements Tokenizer {
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "\\b(?:abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|" +
                    "default|do|double|else|enum|extends|final|finally|float|for|if|implements|import|" +
                    "instanceof|int|interface|long|native|new|package|private|protected|public|return|" +
                    "short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|" +
                    "void|volatile|while)\\b|" + // Keywords
                    "\\b[a-zA-Z_]\\w*\\b|" +    // Identifiers
                    "0[xX][0-9a-fA-F]+|\\d+\\.?\\d*|" + // Numbers
                    "\"[^\"]*\"|'[^']*'|" +     // Strings and chars
                    "==|!=|<=|>=|&&|\\|\\||\\+\\+|--|->|" + // Operators
                    "[\\{\\}\\(\\)\\[\\]\\.;,]|" + // Separators
                    "[+\\-*/%&|^~!=<>?:]"        // Operators
    );
    @Override
    public List<String> extractTokens(String code) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(code);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }
}