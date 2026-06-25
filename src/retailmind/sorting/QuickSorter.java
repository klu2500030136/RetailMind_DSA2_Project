package retailmind.sorting;

import java.util.List;

/**
 * Implements Quick Sort for ranking products by Popularity (sales count) in descending order.
 */
public class QuickSorter {

    /**
     * Sorts a list of product statistics by popularity score (descending).
     *
     * @param stats list of ProductStats
     */
    public static void sortByPopularity(List<ProductStats> stats) {
        if (stats == null || stats.size() <= 1) {
            return;
        }
        quickSort(stats, 0, stats.size() - 1);
    }

    private static void quickSort(List<ProductStats> stats, int low, int high) {
        if (low < high) {
            int pi = partition(stats, low, high);
            quickSort(stats, low, pi - 1);
            quickSort(stats, pi + 1, high);
        }
    }

    private static int partition(List<ProductStats> stats, int low, int high) {
        double pivot = stats.get(high).getPopularityScore();
        int i = (low - 1);

        // Descending order
        for (int j = low; j < high; j++) {
            if (stats.get(j).getPopularityScore() >= pivot) {
                i++;
                swap(stats, i, j);
            }
        }
        swap(stats, i + 1, high);
        return i + 1;
    }

    private static void swap(List<ProductStats> stats, int i, int j) {
        ProductStats temp = stats.get(i);
        stats.set(i, stats.get(j));
        stats.set(j, temp);
    }
}
