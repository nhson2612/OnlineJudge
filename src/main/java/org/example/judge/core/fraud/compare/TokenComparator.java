package org.example.judge.core.fraud.compare;

import org.example.judge.core.fraud.domain.DetectionContext;

import java.util.*;

public class TokenComparator {
    private static final double SIMILARITY_THRESHOLD = 0.85;

    public double compare(DetectionContext context1, DetectionContext context2) {
        List<String> tokens1 = context1.getTokens();
        List<String> tokens2 = context2.getTokens();

        double lengthSimilarity = compareTokenLength(tokens1, tokens2);
        double frequencySimilarity = compareTokenFrequency(
                context1.getTokenFrequency(),
                context2.getTokenFrequency()
        );
        double sequenceSimilarity = compareTokenSequence(tokens1, tokens2);
        double similarityScore = (0.2 * lengthSimilarity)
                + (0.3 * frequencySimilarity)
                + (0.5 * sequenceSimilarity);

        return similarityScore;
    }

    private double compareTokenLength(List<String> tokens1, List<String> tokens2) {
        int len1 = tokens1.size();
        int len2 = tokens2.size();

        if (len1 == 0 || len2 == 0) {
            return 0.0;
        }

        return 1.0 - (double)Math.abs(len1 - len2) / Math.max(len1, len2);
    }
    private double compareTokenFrequency(
            Map<String, Integer> freq1,
            Map<String, Integer> freq2
    ) {
        Set<String> allTokens = new HashSet<>();
        allTokens.addAll(freq1.keySet());
        allTokens.addAll(freq2.keySet());

        if (allTokens.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (String token : allTokens) {
            int count1 = freq1.getOrDefault(token, 0);
            int count2 = freq2.getOrDefault(token, 0);
            int max = Math.max(count1, count2);
            if (max == 0) continue;
            sum += 1.0 - (double)Math.abs(count1 - count2) / max;
        }

        return sum / allTokens.size();
    }
    private double compareTokenSequence(List<String> tokens1, List<String> tokens2) {
        if (tokens1.isEmpty() || tokens2.isEmpty()) {
            return 0.0;
        }

        int[][] dp = new int[tokens1.size() + 1][tokens2.size() + 1];

        for (int i = 1; i <= tokens1.size(); i++) {
            for (int j = 1; j <= tokens2.size(); j++) {
                if (tokens1.get(i - 1).equals(tokens2.get(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        int lcsLength = dp[tokens1.size()][tokens2.size()];
        return (double) lcsLength / Math.min(tokens1.size(), tokens2.size());
    }
    public boolean isPotentialFraud(double similarityScore) {
        return similarityScore >= SIMILARITY_THRESHOLD;
    }
    public Map<String, Double> compareAll(
            DetectionContext targetContext,
            List<DetectionContext> sourceContexts
    ) {
        Map<String, Double> results = new HashMap<>();

        for (DetectionContext sourceContext : sourceContexts) {
            double score = compare(targetContext, sourceContext);
            results.put(sourceContext.getSourceCodePath(), score);
        }

        return results;
    }
}
