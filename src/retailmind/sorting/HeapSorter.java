package retailmind.sorting;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements Heap Sort to get the Top N selling products (by sales count).
 */
public class HeapSorter {

    /**
     * Extracts the top N products using a Max Heap.
     *
     * @param stats list of ProductStats
     * @param n number of top products to retrieve
     * @return list of top N ProductStats
     */
    public static List<ProductStats> getTopSellingProducts(List<ProductStats> stats, int n) {
        if (stats == null || stats.isEmpty() || n <= 0) {
            return new ArrayList<>();
        }

        List<ProductStats> heapList = new ArrayList<>(stats);
        int size = heapList.size();

        // Build max heap
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapify(heapList, size, i);
        }

        List<ProductStats> topN = new ArrayList<>();
        int count = Math.min(n, size);

        // Extract elements from heap one by one
        for (int i = size - 1; i >= size - count; i--) {
            topN.add(heapList.get(0)); // Get max element
            swap(heapList, 0, i); // Move max to the end
            heapify(heapList, i, 0); // Heapify the reduced heap
        }

        return topN;
    }

    private static void heapify(List<ProductStats> heapList, int size, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < size && heapList.get(left).getSalesCount() > heapList.get(largest).getSalesCount()) {
            largest = left;
        }

        if (right < size && heapList.get(right).getSalesCount() > heapList.get(largest).getSalesCount()) {
            largest = right;
        }

        if (largest != i) {
            swap(heapList, i, largest);
            heapify(heapList, size, largest);
        }
    }

    private static void swap(List<ProductStats> stats, int i, int j) {
        ProductStats temp = stats.get(i);
        stats.set(i, stats.get(j));
        stats.set(j, temp);
    }
}
