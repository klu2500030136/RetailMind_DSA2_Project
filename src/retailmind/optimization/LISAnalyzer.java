package retailmind.optimization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements Longest Increasing Subsequence (Dynamic Programming) to analyze sales trends.
 */
public class LISAnalyzer {

    public static class LISResult {
        public List<Double> sequence;
        public int trendLength;

        public LISResult(List<Double> sequence, int trendLength) {
            this.sequence = sequence;
            this.trendLength = trendLength;
        }
    }

    /**
     * Finds the longest strictly increasing sequence of revenues.
     * 
     * @param revenues list of daily revenue figures
     * @return LISResult containing the sequence and its length
     */
    public static LISResult analyzeGrowthTrend(List<Double> revenues) {
        if (revenues == null || revenues.isEmpty()) {
            return new LISResult(new ArrayList<>(), 0);
        }

        int n = revenues.size();
        int[] dp = new int[n];
        int[] prev = new int[n];

        // Initialize DP arrays
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            prev[i] = -1;
        }

        int maxLength = 1;
        int bestEndIdx = 0;

        // Compute LIS
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (revenues.get(i) > revenues.get(j) && dp[i] < dp[j] + 1) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                bestEndIdx = i;
            }
        }

        // Reconstruct path
        List<Double> sequence = new ArrayList<>();
        int curr = bestEndIdx;
        while (curr != -1) {
            sequence.add(revenues.get(curr));
            curr = prev[curr];
        }

        Collections.reverse(sequence);
        return new LISResult(sequence, maxLength);
    }
}
