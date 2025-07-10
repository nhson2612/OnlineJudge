package org.example.judge.core.fraud.processor;

import org.example.judge.core.fraud.domain.DetectionContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentRemoveStage implements FraudStage<DetectionContext,DetectionContext>{

    @Override
    public DetectionContext process(DetectionContext input) {
        String code = input.getRawCode();
        String stringLiteralPattern = "\"([^\"\\\\]|\\\\.)*\""; // literal
        String blockCommentPattern = "/\\*.*?\\*/"; // comment khối
        String lineCommentPattern = "//.*|#.*"; //... và #...
        String pattern = stringLiteralPattern + "|" + blockCommentPattern + "|" + lineCommentPattern;

        Pattern regex = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher matcher = regex.matcher(code);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String match = matcher.group();
            if (match.startsWith("/*") || match.startsWith("//") || match.startsWith("#")) {
                matcher.appendReplacement(sb, "");
            } else {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(match));
            }
        }
        matcher.appendTail(sb);
        input.setCodeWithoutComments(sb.toString().trim());
        return input;
    }
}
