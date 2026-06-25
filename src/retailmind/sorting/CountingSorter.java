package retailmind.sorting;

import java.util.ArrayList;
import java.util.List;

import retailmind.model.Product;

/**
 * Implements Counting Sort for ranking products by Stock Quantity in ascending order.
 * Works only for non-negative stock values.
 */
public class CountingSorter {

    /**
     * Sorts a list of products by stock quantity (ascending).
     *
     * @param products list of Product objects
     */
    public static void sortByStock(List<Product> products) {
        if (products == null || products.size() <= 1) {
            return;
        }

        int maxStock = 0;
        for (Product p : products) {
            if (p.getStockQuantity() < 0) {
                // If any negative stock is found, counting sort shouldn't be used directly. 
                // We'll skip sorting or handle as 0 for safety, but typically retail has >= 0.
                System.out.println("Warning: Negative stock found for product ID " + p.getProductId() + ". Treating as 0 for sort.");
                p.setStockQuantity(Math.max(0, p.getStockQuantity()));
            }
            if (p.getStockQuantity() > maxStock) {
                maxStock = p.getStockQuantity();
            }
        }

        int[] countArray = new int[maxStock + 1];
        for (Product p : products) {
            countArray[p.getStockQuantity()]++;
        }

        for (int i = 1; i <= maxStock; i++) {
            countArray[i] += countArray[i - 1];
        }

        Product[] outputArray = new Product[products.size()];
        for (int i = products.size() - 1; i >= 0; i--) {
            Product current = products.get(i);
            int stock = current.getStockQuantity();
            outputArray[countArray[stock] - 1] = current;
            countArray[stock]--;
        }

        for (int i = 0; i < products.size(); i++) {
            products.set(i, outputArray[i]);
        }
    }
}
