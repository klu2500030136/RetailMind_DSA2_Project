package retailmind.optimization;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements 0/1 Knapsack Algorithm (Dynamic Programming) for Inventory Stock Optimization.
 */
public class ZeroOneKnapsackOptimizer {

    public static class ZeroOneResult {
        public List<ShelfItem> selectedItems;
        public double maxProfit;

        public ZeroOneResult(List<ShelfItem> selectedItems, double maxProfit) {
            this.selectedItems = selectedItems;
            this.maxProfit = maxProfit;
        }
    }

    /**
     * Selects whole products to maximize profit within warehouse capacity.
     * Uses Dynamic Programming. Assumes weights/capacities are convertible to integers for DP table.
     * 
     * @param items list of products with space and profit
     * @param capacity integer warehouse capacity
     * @return ZeroOneResult containing selected items and max profit
     */
    public static ZeroOneResult optimizeInventory(List<ShelfItem> items, int capacity) {
        if (items == null || items.isEmpty() || capacity <= 0) {
            return new ZeroOneResult(new ArrayList<>(), 0.0);
        }

        int n = items.size();
        double[][] dp = new double[n + 1][capacity + 1];

        // Build DP table
        for (int i = 1; i <= n; i++) {
            ShelfItem item = items.get(i - 1);
            int weight = (int) Math.ceil(item.getShelfSpace()); // converting space to int for DP

            for (int w = 1; w <= capacity; w++) {
                if (weight <= w) {
                    dp[i][w] = Math.max(item.getProfit() + dp[i - 1][w - weight], dp[i - 1][w]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        double maxProfit = dp[n][capacity];
        List<ShelfItem> selectedItems = new ArrayList<>();
        
        // Backtrack to find selected items
        double res = maxProfit;
        int w = capacity;
        for (int i = n; i > 0 && res > 0; i--) {
            if (res != dp[i - 1][w]) {
                // This item was included
                ShelfItem includedItem = items.get(i - 1);
                selectedItems.add(includedItem);
                
                int weight = (int) Math.ceil(includedItem.getShelfSpace());
                res -= includedItem.getProfit();
                w -= weight;
            }
        }

        return new ZeroOneResult(selectedItems, maxProfit);
    }
}
