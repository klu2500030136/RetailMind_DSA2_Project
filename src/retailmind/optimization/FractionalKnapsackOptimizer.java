package retailmind.optimization;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements Fractional Knapsack Algorithm (Greedy) for Shelf Space Optimization.
 */
public class FractionalKnapsackOptimizer {

    public static class FractionalResult {
        public List<String> selectedItems;
        public double maxProfit;

        public FractionalResult(List<String> selectedItems, double maxProfit) {
            this.selectedItems = selectedItems;
            this.maxProfit = maxProfit;
        }
    }

    /**
     * Optimizes shelf utilization. Allows fractional selection of products.
     *
     * @param items list of shelf items
     * @param capacity total shelf capacity
     * @return FractionalResult containing selected details and max profit
     */
    public static FractionalResult optimizeShelfSpace(List<ShelfItem> items, double capacity) {
        if (items == null || items.isEmpty() || capacity <= 0) {
            return new FractionalResult(new ArrayList<>(), 0.0);
        }

        // Sort items by profit per unit space descending
        List<ShelfItem> sortedItems = new ArrayList<>(items);
        sortedItems.sort((a, b) -> Double.compare(b.getProfitPerSpace(), a.getProfitPerSpace()));

        double currentCapacity = capacity;
        double totalProfit = 0.0;
        List<String> selectionDetails = new ArrayList<>();

        for (ShelfItem item : sortedItems) {
            if (currentCapacity == 0) {
                break;
            }

            if (item.getShelfSpace() <= currentCapacity) {
                // Can take the whole item
                currentCapacity -= item.getShelfSpace();
                totalProfit += item.getProfit();
                selectionDetails.add(item.toString() + " : 100%");
            } else {
                // Take a fraction
                double fraction = currentCapacity / item.getShelfSpace();
                totalProfit += (item.getProfit() * fraction);
                int percent = (int) (fraction * 100);
                selectionDetails.add(item.toString() + " : " + percent + "%");
                currentCapacity = 0; // Knapsack is full
            }
        }

        return new FractionalResult(selectionDetails, totalProfit);
    }
}
