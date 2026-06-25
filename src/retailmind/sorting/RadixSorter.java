package retailmind.sorting;

import java.util.ArrayList;
import java.util.List;

import retailmind.model.Product;

/**
 * Implements Radix Sort for ranking products by Barcode in ascending order.
 */
public class RadixSorter {

    /**
     * Sorts a list of products by numeric barcode (ascending).
     * Non-numeric barcodes will be treated as having a value of 0.
     *
     * @param products list of Product objects
     */
    public static void sortByBarcode(List<Product> products) {
        if (products == null || products.size() <= 1) {
            return;
        }

        long maxBarcode = 0;
        List<Long> parsedBarcodes = new ArrayList<>();

        for (Product p : products) {
            long barcodeVal = parseBarcode(p.getBarcode());
            parsedBarcodes.add(barcodeVal);
            if (barcodeVal > maxBarcode) {
                maxBarcode = barcodeVal;
            }
        }

        for (long exp = 1; maxBarcode / exp > 0; exp *= 10) {
            countingSortByDigit(products, parsedBarcodes, exp);
        }
    }

    private static void countingSortByDigit(List<Product> products, List<Long> parsedBarcodes, long exp) {
        int n = products.size();
        Product[] outputProducts = new Product[n];
        long[] outputBarcodes = new long[n];
        int[] count = new int[10];

        for (int i = 0; i < n; i++) {
            int digit = (int) ((parsedBarcodes.get(i) / exp) % 10);
            count[digit]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            int digit = (int) ((parsedBarcodes.get(i) / exp) % 10);
            int index = count[digit] - 1;
            outputProducts[index] = products.get(i);
            outputBarcodes[index] = parsedBarcodes.get(i);
            count[digit]--;
        }

        for (int i = 0; i < n; i++) {
            products.set(i, outputProducts[i]);
            parsedBarcodes.set(i, outputBarcodes[i]);
        }
    }

    private static long parseBarcode(String barcode) {
        if (barcode == null || barcode.trim().isEmpty()) {
            return 0;
        }
        try {
            return Long.parseLong(barcode.trim());
        } catch (NumberFormatException e) {
            // Non-numeric barcodes treated as 0 to avoid crash
            return 0;
        }
    }
}
