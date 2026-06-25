package retailmind.sorting;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements Merge Sort for ranking products by Revenue in descending order.
 */
public class MergeSorter {

    /**
     * Sorts a list of product statistics by total revenue (descending).
     *
     * @param stats list of ProductStats
     */
    public static void sortByRevenue(List<ProductStats> stats) {
        if (stats == null || stats.size() <= 1) {
            return;
        }
        mergeSort(stats, 0, stats.size() - 1);
    }

    private static void mergeSort(List<ProductStats> stats, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(stats, left, mid);
            mergeSort(stats, mid + 1, right);
            merge(stats, left, mid, right);
        }
    }

    private static void merge(List<ProductStats> stats, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        List<ProductStats> leftList = new ArrayList<>(n1);
        List<ProductStats> rightList = new ArrayList<>(n2);

        for (int i = 0; i < n1; ++i) {
            leftList.add(stats.get(left + i));
        }
        for (int j = 0; j < n2; ++j) {
            rightList.add(stats.get(mid + 1 + j));
        }

        int i = 0, j = 0;
        int k = left;

        // Descending order
        while (i < n1 && j < n2) {
            if (leftList.get(i).getTotalRevenue() >= rightList.get(j).getTotalRevenue()) {
                stats.set(k, leftList.get(i));
                i++;
            } else {
                stats.set(k, rightList.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            stats.set(k, leftList.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            stats.set(k, rightList.get(j));
            j++;
            k++;
        }
    }
}
