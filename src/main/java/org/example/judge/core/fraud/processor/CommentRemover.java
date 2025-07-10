package org.example.judge.core.fraud.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentRemover {
    public String removeComments(String codeLine) {
        String lineCommentPattern = "//.*|#.*"; // Matches // or # comments
        String blockCommentPattern = "/\\*.*?\\*/"; // Matches /* */ comments
        String stringLiteralPattern = "\"([^\"\\\\]|\\\\.)*\"";
        String pattern = stringLiteralPattern + "|" + blockCommentPattern + "|" + lineCommentPattern;

        Matcher matcher = Pattern.compile(pattern).matcher(codeLine);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            if (matcher.group().startsWith("/*") ||
                    matcher.group().startsWith("//") ||
                    matcher.group().startsWith("#")) {
                matcher.appendReplacement(sb, "");
            } else {
                matcher.appendReplacement(sb, matcher.group());
            }
        }
        matcher.appendTail(sb);
        return sb.toString().trim();
    }
}
