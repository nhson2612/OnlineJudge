package org.example.judge.core.fraud.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DetectionContext {
    private final String sourceCodePath;
    private final String rawCode;                // giữ nguyên, không bao giờ bị thay đổi
    private String codeWithoutComments;          // bước 1
    private String normalizedCode;               // bước 2: chuẩn hóa (remove whitespace, format)
    private String reorderedCode;                // bước 3: sắp xếp lại thứ tự hàm
    private List<String> tokens;                 // bước 4: phân tích token
    private Map<String, Integer> tokenFrequency; // bước 5: tần suất token

    public DetectionContext(String sourceCodePath, String rawCode) {
        this.sourceCodePath = sourceCodePath;
        this.rawCode = rawCode;
    }

    public String getRawCode() {
        return rawCode;
    }

    public String getSourceCodePath() {
        return sourceCodePath;
    }

    public String getCodeWithoutComments() {
        return codeWithoutComments;
    }

    public void setCodeWithoutComments(String codeWithoutComments) {
        this.codeWithoutComments = codeWithoutComments;
    }

    public String getNormalizedCode() {
        return normalizedCode;
    }

    public void setNormalizedCode(String normalizedCode) {
        this.normalizedCode = normalizedCode;
    }

    public String getReorderedCode() {
        return reorderedCode;
    }

    public void setReorderedCode(String reorderedCode) {
        this.reorderedCode = reorderedCode;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
        this.tokenFrequency = null;
    }
    public Map<String, Integer> getTokenFrequency() {
        if (this.tokenFrequency == null && this.tokens != null) {
            calculateTokenFrequency();
        }
        return this.tokenFrequency;
    }

    private void calculateTokenFrequency() {
        if (this.tokens == null || this.tokens.isEmpty()) {
            this.tokenFrequency = Collections.emptyMap();
            return;
        }
        this.tokenFrequency = this.tokens.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        token -> token,
                        token -> 1,
                        Integer::sum
                ));
    }
}
