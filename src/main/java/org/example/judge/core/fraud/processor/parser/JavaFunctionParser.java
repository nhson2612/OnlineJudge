package org.example.judge.core.fraud.processor.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaFunctionParser implements FunctionParser {
    private static final Pattern FUNCTION_PATTERN = Pattern.compile(
            "(?s)((?:@\\w+\\s+)*)(public|protected|private|static|final|native|synchronized|abstract|transient|\\s)+" +
                    "[\\w<>\\[\\]]+\\s+(\\w+)\\s*\\([^)]*\\)\\s*(?:throws\\s+[\\w,\\s]+)?\\s*\\{.*?\\}(?=\\s*(?:@|public|private|protected|class|interface|$))"
    );

    @Override
    public List<FunctionBlock> extractFunctions(String code) {
        List<FunctionBlock> functions = new ArrayList<>();
        Matcher matcher = FUNCTION_PATTERN.matcher(code);

        while (matcher.find()) {
            String funcName = matcher.group(3);
            functions.add(new FunctionBlock(
                    funcName,
                    matcher.group(0),
                    matcher.start(),
                    matcher.end()
            ));
        }
        return functions;
    }
}