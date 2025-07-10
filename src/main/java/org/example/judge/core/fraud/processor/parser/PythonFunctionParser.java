package org.example.judge.core.fraud.processor.parser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PythonFunctionParser implements FunctionParser {

    private static final Pattern FUNCTION_PATTERN = Pattern.compile(
            "(?m)^\\s*(?:async\\s+)?def\\s+(\\w+)\\s*\\([^)]*\\)\\s*:\\s*(#.*)?$"
    );

    @Override
    public List<FunctionBlock> extractFunctions(String code) {
        List<FunctionBlock> functions = new ArrayList<>();
        Matcher matcher = FUNCTION_PATTERN.matcher(code);
        List<int[]> funcPositions = new ArrayList<>();
        while (matcher.find()) {
            int start = matcher.start();
            int lineStart = start;

            int nextFuncStart = code.length();
            if (matcher.find()) {
                nextFuncStart = matcher.start();
                matcher.region(start, code.length());
            }
            matcher.region(nextFuncStart, code.length());
            funcPositions.add(new int[]{lineStart, nextFuncStart});
        }

        for (int[] pos : funcPositions) {
            int start = pos[0], end = pos[1];
            String funcBlock = code.substring(start, end).trim();
            Matcher nameMatcher = Pattern.compile("def\\s+(\\w+)").matcher(funcBlock);
            String name = nameMatcher.find() ? nameMatcher.group(1) : "unknown";
            functions.add(new FunctionBlock(name, funcBlock, start, end));
        }
        return functions;
    }
}
