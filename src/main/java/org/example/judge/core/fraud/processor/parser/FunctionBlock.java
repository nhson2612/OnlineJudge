package org.example.judge.core.fraud.processor.parser;

public class FunctionBlock {
    private final String name;
    private final String content;
    private final int startPosition;
    private final int endPosition;

    public FunctionBlock(String name, String content, int start, int end) {
        this.name = name;
        this.content = content;
        this.startPosition = start;
        this.endPosition = end;
    }

    public String getName() { return name; }
    public String getContent() { return content; }
    public int getStartPosition() { return startPosition; }
    public int getEndPosition() { return endPosition; }
}
