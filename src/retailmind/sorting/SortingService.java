package retailmind.sorting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retailmind.manager.InventoryManager;
import retailmind.manager.TransactionManager;
import retailmind.model.Product;
import retailmind.model.Transaction;

/**
 * Service to coordinate all sorting operations and generate reports.
 */
public class SortingService {
    private final InventoryManager inventoryManager;
    private final TransactionManager transactionManager;
    private static final String REPORTS_DIR = "reports/";

    public SortingService(InventoryManager inventoryManager, TransactionManager transactionManager) {
        this.inventoryManager = inventoryManager;
        this.transactionManager = transactionManager;
        createReportsDirectory();
    }

    private void createReportsDirectory() {
        File dir = new File(REPORTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Loads product statistics by combining inventory and transactions.
     * Products with zero sales will be included.
     *
     * @return List of ProductStats
     */
    public List<ProductStats> loadProductStatistics() {
        List<Product> products = inventoryManager.getProducts();
        List<Transaction> transactions = transactionManager.getTransactions();

        Map<Integer, ProductStats> statsMap = new HashMap<>();

        // Initialize map with 0 sales for all products to handle zero-sales products
        for (Product product : products) {
            if (product != null) {
                statsMap.put(product.getProductId(), new ProductStats(product, 0, 0.0, 0.0));
            }
        }

        // Aggregate transactions
        for (Transaction t : transactions) {
            if (t != null && statsMap.containsKey(t.getProductId())) {
                ProductStats stats = statsMap.get(t.getProductId());
                stats.setSalesCount(stats.getSalesCount() + t.getQuantity());
                stats.setTotalRevenue(stats.getTotalRevenue() + t.getTotalAmount());
                stats.setPopularityScore(stats.getSalesCount()); // Popularity = Sales Count
            }
        }

        return new ArrayList<>(statsMap.values());
    }

    public void sortByRevenue() {
        List<ProductStats> stats = loadProductStatistics();
        if (stats.isEmpty()) {
            System.out.println("No products available to sort.");
            return;
        }

        long startTime = System.nanoTime();
        MergeSorter.sortByRevenue(stats);
        long endTime = System.nanoTime();

        System.out.println("Products sorted by Revenue (Descending) using Merge Sort.");
        System.out.println("Execution Time: " + (endTime - startTime) + " ns");
        
        displayProductStatsTable(stats);
        generateSortedProductReport(stats, "Merge Sort");
    }

    public void sortByPopularity() {
        List<ProductStats> stats = loadProductStatistics();
        if (stats.isEmpty()) {
            System.out.println("No products available to sort.");
            return;
        }

        long startTime = System.nanoTime();
        QuickSorter.sortByPopularity(stats);
        long endTime = System.nanoTime();

        System.out.println("Products sorted by Popularity (Descending) using Quick Sort.");
        System.out.println("Execution Time: " + (endTime - startTime) + " ns");

        displayProductStatsTable(stats);
        generateSortedProductReport(stats, "Quick Sort");
    }

    public void displayTopSellingProducts(int topN) {
        if (topN <= 0) {
            System.out.println("Invalid Top N value. Must be greater than 0.");
            return;
        }

        List<ProductStats> stats = loadProductStatistics();
        if (stats.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        long startTime = System.nanoTime();
        List<ProductStats> topSelling = HeapSorter.getTopSellingProducts(stats, topN);
        long endTime = System.nanoTime();

        System.out.println("Top " + topSelling.size() + " Selling Products generated using Heap Sort.");
        System.out.println("Execution Time: " + (endTime - startTime) + " ns");

        System.out.println("\n---");
        System.out.printf("%-6s | %-15s | %-6s%n", "Rank", "Product", "Sales");
        System.out.println("-------------------------------------");
        for (int i = 0; i < topSelling.size(); i++) {
            System.out.printf("%-6d | %-15s | %-6d%n",
                    (i + 1),
                    topSelling.get(i).getProduct().getProductName(),
                    topSelling.get(i).getSalesCount());
        }
        System.out.println("---");

        generateTopSellingReport(topSelling);
    }

    public void sortByStock() {
        List<Product> products = inventoryManager.getProducts();
        if (products.isEmpty()) {
            System.out.println("No products available to sort.");
            return;
        }

        long startTime = System.nanoTime();
        CountingSorter.sortByStock(products);
        long endTime = System.nanoTime();

        System.out.println("Products sorted by Stock Quantity (Ascending) using Counting Sort.");
        System.out.println("Execution Time: " + (endTime - startTime) + " ns");

        displayProductsTable(products);
    }

    public void sortByBarcode() {
        List<Product> products = inventoryManager.getProducts();
        if (products.isEmpty()) {
            System.out.println("No products available to sort.");
            return;
        }

        long startTime = System.nanoTime();
        RadixSorter.sortByBarcode(products);
        long endTime = System.nanoTime();

        System.out.println("Products sorted by Barcode (Ascending) using Radix Sort.");
        System.out.println("Execution Time: " + (endTime - startTime) + " ns");

        displayProductsTable(products);
    }

    private void displayProductStatsTable(List<ProductStats> stats) {
        System.out.println("\n---");
        System.out.printf("%-6s | %-15s | %-6s | %-10s | %-15s%n", "Rank", "Product", "Sales", "Revenue", "Barcode");
        System.out.println("------------------------------------------------------------------");
        int rank = 1;
        for (ProductStats stat : stats) {
            Product p = stat.getProduct();
            System.out.printf("%-6d | %-15s | %-6d | %-10.2f | %-15s%n",
                    rank++,
                    p != null ? p.getProductName() : "Unknown",
                    stat.getSalesCount(),
                    stat.getTotalRevenue(),
                    p != null ? p.getBarcode() : "N/A");
        }
        System.out.println("---");
    }

    private void displayProductsTable(List<Product> products) {
        System.out.println("\n---");
        System.out.printf("%-6s | %-15s | %-8s | %-10s | %-15s%n", "No.", "Product", "Stock", "Price", "Barcode");
        System.out.println("------------------------------------------------------------------");
        int index = 1;
        for (Product p : products) {
            if (p != null) {
                System.out.printf("%-6d | %-15s | %-8d | %-10.2f | %-15s%n",
                        index++,
                        p.getProductName(),
                        p.getStockQuantity(),
                        p.getPrice(),
                        p.getBarcode());
            }
        }
        System.out.println("---");
    }

    public void generateSortedProductReport(List<ProductStats> stats, String algorithmName) {
        if (stats == null || stats.isEmpty()) return;

        String filename = REPORTS_DIR + "sorted_products.txt";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("=========================================\n");
            writer.write("Sorted Products Report\n");
            writer.write("Generated On: " + timestamp + "\n");
            writer.write("Algorithm Used: " + algorithmName + "\n");
            writer.write("Total Products: " + stats.size() + "\n");
            writer.write("=========================================\n\n");

            writer.write(String.format("%-6s | %-15s | %-10s | %-6s%n", "Rank", "Product", "Revenue", "Sales"));
            writer.write("------------------------------------------------------\n");

            int rank = 1;
            for (ProductStats stat : stats) {
                Product p = stat.getProduct();
                writer.write(String.format("%-6d | %-15s | %-10.2f | %-6d%n",
                        rank++,
                        p != null ? p.getProductName() : "Unknown",
                        stat.getTotalRevenue(),
                        stat.getSalesCount()));
            }
            writer.write("=========================================\n");
            System.out.println("Report successfully generated: " + filename);
        } catch (IOException e) {
            System.out.println("Failed to generate sorted products report: " + e.getMessage());
        }
    }

    public void generateTopSellingReport(List<ProductStats> topSelling) {
        if (topSelling == null || topSelling.isEmpty()) return;

        String filename = REPORTS_DIR + "top_selling_products.txt";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("=========================================\n");
            writer.write("Top Selling Products\n");
            writer.write("Generated On: " + timestamp + "\n");
            writer.write("=========================================\n\n");

            int rank = 1;
            for (ProductStats stat : topSelling) {
                Product p = stat.getProduct();
                writer.write(rank++ + ". " + (p != null ? p.getProductName() : "Unknown") + " (Sales: " + stat.getSalesCount() + ")\n");
            }
            writer.write("\n=========================================\n");
            System.out.println("Report successfully generated: " + filename);
        } catch (IOException e) {
            System.out.println("Failed to generate top selling products report: " + e.getMessage());
        }
    }

    public void compareAlgorithms() {
        List<ProductStats> statsTemplate = loadProductStatistics();
        List<Product> productsTemplate = inventoryManager.getProducts();

        if (statsTemplate.isEmpty() || productsTemplate.isEmpty()) {
            System.out.println("Insufficient data for comparison.");
            return;
        }

        System.out.println("\nRunning Algorithm Comparison...");
        
        List<String[]> results = new ArrayList<>();

        // Merge Sort
        List<ProductStats> mergeList = new ArrayList<>(statsTemplate);
        long start = System.nanoTime();
        MergeSorter.sortByRevenue(mergeList);
        long end = System.nanoTime();
        results.add(new String[]{"Merge Sort", (end - start) + " ns", "O(n log n)", "Stable"});

        // Quick Sort
        List<ProductStats> quickList = new ArrayList<>(statsTemplate);
        start = System.nanoTime();
        QuickSorter.sortByPopularity(quickList);
        end = System.nanoTime();
        results.add(new String[]{"Quick Sort", (end - start) + " ns", "O(n log n)", "Unstable"});

        // Heap Sort
        List<ProductStats> heapList = new ArrayList<>(statsTemplate);
        start = System.nanoTime();
        HeapSorter.getTopSellingProducts(heapList, heapList.size());
        end = System.nanoTime();
        results.add(new String[]{"Heap Sort", (end - start) + " ns", "O(n log n)", "Unstable"});

        // Counting Sort
        List<Product> countingList = new ArrayList<>(productsTemplate);
        start = System.nanoTime();
        CountingSorter.sortByStock(countingList);
        end = System.nanoTime();
        results.add(new String[]{"Counting Sort", (end - start) + " ns", "O(n + k)", "Stable"});

        // Radix Sort
        List<Product> radixList = new ArrayList<>(productsTemplate);
        start = System.nanoTime();
        RadixSorter.sortByBarcode(radixList);
        end = System.nanoTime();
        results.add(new String[]{"Radix Sort", (end - start) + " ns", "O(d(n + k))", "Stable"});

        // Display
        System.out.println("\n-------------------------------------------------------------------------");
        System.out.printf("%-15s | %-15s | %-15s | %-10s%n", "Algorithm Name", "Execution Time", "Time Complexity", "Stability");
        System.out.println("-------------------------------------------------------------------------");
        for (String[] res : results) {
            System.out.printf("%-15s | %-15s | %-15s | %-10s%n", res[0], res[1], res[2], res[3]);
        }
        System.out.println("-------------------------------------------------------------------------");

        generateComparisonReport(results);
    }

    private void generateComparisonReport(List<String[]> results) {
        String filename = REPORTS_DIR + "sorting_comparison_report.txt";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("=========================================\n");
            writer.write("Sorting Algorithms Comparison Report\n");
            writer.write("Generated On: " + timestamp + "\n");
            writer.write("=========================================\n\n");

            writer.write(String.format("%-15s | %-15s | %-15s | %-10s%n", "Algorithm Name", "Execution Time", "Time Complexity", "Stability"));
            writer.write("-------------------------------------------------------------------------\n");
            for (String[] res : results) {
                writer.write(String.format("%-15s | %-15s | %-15s | %-10s%n", res[0], res[1], res[2], res[3]));
            }
            writer.write("\n=========================================\n");
            System.out.println("Comparison report saved to: " + filename);
        } catch (IOException e) {
            System.out.println("Failed to generate comparison report: " + e.getMessage());
        }
    }
}
