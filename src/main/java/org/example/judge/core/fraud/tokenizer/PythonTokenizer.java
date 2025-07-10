package org.example.judge.core.fraud.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PythonTokenizer implements Tokenizer {
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "\\b(?:and|as|assert|break|class|continue|def|del|elif|else|except|finally|for|" +
                    "from|global|if|import|in|is|lambda|nonlocal|not|or|pass|raise|return|try|while|" +
                    "with|yield|True|False|None)\\b|" + // Keywords
                    "\\b[a-zA-Z_]\\w*\\b|" +    // Identifiers
                    "0[xX][0-9a-fA-F]+|\\d+\\.?\\d*|" + // Numbers
                    "\"[^\"]*\"|'[^']*'|" +     // Strings
                    "==|!=|<=|>=|\\*\\*|//|<<|>>|" + // Operators
                    "[\\{\\}\\(\\)\\[\\],:]|" + // Separators
                    "[+\\-*/%&|^~!=<>@]"        // Operators
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
