package retailmind.optimization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retailmind.manager.InventoryManager;
import retailmind.model.Product;

/**
 * Service to orchestrate Business Optimization Algorithms.
 */
public class OptimizationService {
    private final InventoryManager inventoryManager;
    private static final String REPORTS_DIR = "reports/";
    private static final String DATA_DIR = "data/";
    private static final String PROMOTIONS_FILE = DATA_DIR + "promotions.txt";
    private static final String SALES_FILE = DATA_DIR + "sales.txt";

    public OptimizationService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
        createDirectories();
        ensurePromotionsFileExists();
    }

    private void createDirectories() {
        new File(REPORTS_DIR).mkdirs();
        new File(DATA_DIR).mkdirs();
    }

    private void ensurePromotionsFileExists() {
        File file = new File(PROMOTIONS_FILE);
        if (!file.exists() || file.length() == 0) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Festival Sale,1,4,50000\n");
                writer.write("Weekend Offer,3,5,30000\n");
                writer.write("Flash Sale,0,6,25000\n");
                writer.write("Electronics Discount,5,7,45000\n");
            } catch (IOException e) {
                System.out.println("Warning: Could not create default promotions.txt.");
            }
        }
    }

    /**
     * Loads promotions from data/promotions.txt.
     */
    private List<Promotion> loadPromotions() {
        List<Promotion> promotions = new ArrayList<>();
        File file = new File(PROMOTIONS_FILE);
        if (!file.exists()) return promotions;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    try {
                        String name = parts[0].trim();
                        int start = Integer.parseInt(parts[1].trim());
                        int end = Integer.parseInt(parts[2].trim());
                        double profit = Double.parseDouble(parts[3].trim());
                        promotions.add(new Promotion(name, start, end, profit));
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading promotions.txt: " + e.getMessage());
        }
        return promotions;
    }

    /**
     * Schedule Promotions (Activity Selection)
     */
    public void schedulePromotions() {
        List<Promotion> promotions = loadPromotions();
        if (promotions.isEmpty()) {
            System.out.println("No promotions available to schedule.");
            return;
        }

        List<Promotion> selected = ActivitySelectionOptimizer.schedulePromotions(promotions);

        System.out.println("\n=========================================");
        System.out.println("Promotion Schedule");
        System.out.println("==================");
        int i = 1;
        for (Promotion p : selected) {
            System.out.println(i++ + ". " + p.getPromotionName() + " (" + p.getStartTime() + " - " + p.getEndTime() + ")");
        }
        System.out.println("=========================================\n");

        StringBuilder recBuilder = new StringBuilder("Recommended: Run ");
        for (int j = 0; j < selected.size(); j++) {
            recBuilder.append(selected.get(j).getPromotionName());
            if (j < selected.size() - 2) recBuilder.append(", ");
            else if (j == selected.size() - 2) recBuilder.append(" and ");
        }
        recBuilder.append(" to maximize promotional opportunities.");

        String recommendation = recBuilder.toString();
        System.out.println(recommendation);

        savePromotionScheduleReport(selected, recommendation);
    }

    private void savePromotionScheduleReport(List<Promotion> selected, String recommendation) {
        String filename = REPORTS_DIR + "promotion_schedule.txt";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("=========================================\n");
            writer.write("Promotion Schedule\n");
            writer.write("Generated On: " + timestamp + "\n");
            writer.write("Algorithm: Activity Selection (Greedy)\n");
            writer.write("=========================================\n\n");
            
            int i = 1;
            for (Promotion p : selected) {
                writer.write(i++ + ". " + p.getPromotionName() + " (" + p.getStartTime() + " - " + p.getEndTime() + ")\n");
            }

            writer.write("\n" + recommendation + "\n");
            writer.write("=========================================\n");
            System.out.println("Report saved to " + filename);
        } catch (IOException e) {
            System.out.println("Failed to save report: " + e.getMessage());
        }
    }

    /**
     * Converts current inventory into ShelfItems. 
     * Profit is based on price. Space is arbitrarily based on stock quantity or a derived metric.
     */
    private List<ShelfItem> getShelfItems() {
        List<Product> products = inventoryManager.getProducts();
        List<ShelfItem> items = new ArrayList<>();
        for (Product p : products) {
            if (p != null && p.getStockQuantity() > 0) {
                // To make space realistic, assume each stock unit takes 1 space.
                // Profit is item price * quantity to represent total potential value.
                items.add(new ShelfItem(p, p.getStockQuantity(), p.getPrice() * p.getStockQuantity()));
            }
        }
        return items;
    }

    /**
     * Optimize Shelf Space (Fractional Knapsack)
     */
    public void optimizeShelfSpace(double capacity) {
        if (capacity <= 0) {
            System.out.println("Invalid capacity. Must be > 0.");
            return;
        }

        List<ShelfItem> items = getShelfItems();
        if (items.isEmpty()) {
            System.out.println("No items available to optimize.");
            return;
        }

        FractionalKnapsackOptimizer.FractionalResult result = FractionalKnapsackOptimizer.optimizeShelfSpace(items, capacity);

        System.out.println("\n=========================================");
        System.out.println("Shelf Optimization");
        System.out.println("==================");
        for (String detail : result.selectedItems) {
            System.out.println(detail);
        }
        System.out.printf("\nMaximum Profit : %.2f%n", result.maxProfit);
        System.out.println("=========================================\n");

        String recommendation = "Recommended: Allocate more shelf space to high-profit products.";
        System.out.println(recommendation);

        saveStockOptimizationReport("Fractional Knapsack", capacity, result.selectedItems, result.maxProfit, recommendation);
    }

    /**
     * Optimize Inventory Stock (0/1 Knapsack)
     */
    public void optimizeInventoryStock(int capacity) {
        if (capacity <= 0) {
            System.out.println("Invalid warehouse capacity. Must be > 0.");
            return;
        }

        List<ShelfItem> items = getShelfItems();
        if (items.isEmpty()) {
            System.out.println("No items available to optimize.");
            return;
        }

        ZeroOneKnapsackOptimizer.ZeroOneResult result = ZeroOneKnapsackOptimizer.optimizeInventory(items, capacity);

        System.out.println("\n=========================================");
        System.out.println("Stock Optimization (0/1)");
        System.out.println("========================");
        List<String> details = new ArrayList<>();
        for (ShelfItem item : result.selectedItems) {
            details.add(item.toString());
            System.out.println(item.toString());
        }
        System.out.printf("\nMaximum Profit : %.2f%n", result.maxProfit);
        System.out.println("=========================================\n");

        StringBuilder recBuilder = new StringBuilder("Recommended: Prioritize stocking ");
        for (int i = 0; i < details.size(); i++) {
            recBuilder.append(details.get(i));
            if (i < details.size() - 2) recBuilder.append(", ");
            else if (i == details.size() - 2) recBuilder.append(" and ");
        }
        recBuilder.append(".");
        
        String recommendation = recBuilder.toString();
        if (details.isEmpty()) {
            recommendation = "Recommendation: Increase warehouse capacity to fit inventory items.";
        }
        System.out.println(recommendation);

        saveStockOptimizationReport("0/1 Knapsack", capacity, details, result.maxProfit, recommendation);
    }

    private void saveStockOptimizationReport(String algorithm, double capacity, List<String> selectedItems, double maxProfit, String recommendation) {
        String filename = REPORTS_DIR + "stock_optimization.txt";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("=========================================\n");
            writer.write("Stock Optimization Report\n");
            writer.write("Generated On: " + timestamp + "\n");
            writer.write("Algorithm: " + algorithm + "\n");
            writer.write("Capacity: " + capacity + "\n");
            writer.write("=========================================\n\n");

            writer.write("Selected Products:\n");
            for (String item : selectedItems) {
                writer.write(item + "\n");
            }

            writer.write(String.format("\nMaximum Profit: %.2f%n", maxProfit));
            writer.write("\n" + recommendation + "\n");
            writer.write("=========================================\n");
            System.out.println("Report saved to " + filename);
        } catch (IOException e) {
            System.out.println("Failed to save report: " + e.getMessage());
        }
    }

    /**
     * Reads daily revenue from sales.txt. If empty, provides fallback data.
     */
    private List<Double> loadDailyRevenues() {
        List<Double> revenues = new ArrayList<>();
        File file = new File(SALES_FILE);
        
        if (file.exists() && file.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    // sales.txt usually format "Day,Revenue". E.g. "1,5000.0"
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        try {
                            revenues.add(Double.parseDouble(parts[1].trim()));
                        } catch (NumberFormatException ignored) {}
                    } else {
                        // Fallback in case sales.txt just has pure doubles line by line
                        try {
                            revenues.add(Double.parseDouble(line.trim()));
                        } catch (NumberFormatException ignored) {}
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading sales.txt: " + e.getMessage());
            }
        }

        // Fallback if empty or not found to provide a useful demo
        if (revenues.isEmpty()) {
            revenues.add(5000.0);
            revenues.add(6200.0);
            revenues.add(8000.0);
            revenues.add(7500.0);
            revenues.add(9200.0);
            revenues.add(11000.0);
        }

        return revenues;
    }

    /**
     * Analyze Growth Trend (LIS)
     */
    public void analyzeGrowthTrend() {
        List<Double> revenues = loadDailyRevenues();
        LISAnalyzer.LISResult result = LISAnalyzer.analyzeGrowthTrend(revenues);

        System.out.println("\n=========================================");
        System.out.println("Longest Increasing Revenue Trend");
        System.out.println("=========================================");
        for (int i = 0; i < result.sequence.size(); i++) {
            System.out.println(String.format("%.0f", result.sequence.get(i)));
            if (i < result.sequence.size() - 1) {
                System.out.println("->");
            }
        }
        System.out.println("\nTrend Length: " + result.trendLength);
        System.out.println("=========================================\n");

        String recommendation = result.trendLength > 3 
            ? "Recommendation: Revenue trend is increasing consistently. Consider expanding inventory."
            : "Recommendation: Revenue growth is volatile. Focus on promotional offers.";

        System.out.println(recommendation);

        saveGrowthAnalysisReport(result, recommendation);
    }

    private void saveGrowthAnalysisReport(LISAnalyzer.LISResult result, String recommendation) {
        String filename = REPORTS_DIR + "growth_analysis.txt";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("=========================================\n");
            writer.write("Growth Analysis Report\n");
            writer.write("Generated On: " + timestamp + "\n");
            writer.write("Algorithm: Longest Increasing Subsequence (DP)\n");
            writer.write("=========================================\n\n");

            for (int i = 0; i < result.sequence.size(); i++) {
                writer.write(String.format("%.0f", result.sequence.get(i)) + "\n");
                if (i < result.sequence.size() - 1) {
                    writer.write("->\n");
                }
            }

            writer.write("\nTrend Length: " + result.trendLength + "\n");
            writer.write("\n" + recommendation + "\n");
            writer.write("=========================================\n");
            System.out.println("Report saved to " + filename);
        } catch (IOException e) {
            System.out.println("Failed to save report: " + e.getMessage());
        }
    }
}
