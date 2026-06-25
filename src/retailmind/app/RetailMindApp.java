package retailmind.app;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import retailmind.manager.CustomerManager;
import retailmind.manager.InventoryManager;
import retailmind.manager.SupplierManager;
import retailmind.manager.TransactionManager;
import retailmind.model.Customer;
import retailmind.model.Product;
import retailmind.service.GraphNetwork;
import retailmind.service.SalesAnalytics;
import retailmind.sorting.SortingService;
import retailmind.optimization.OptimizationService;
import retailmind.service.ComprehensiveReportService;

/**
 * Entry point for the RetailMind menu-driven console application.
 */
public class RetailMindApp {
    private final Scanner scanner;
    private final InventoryManager inventoryManager;
    private final CustomerManager customerManager;
    private final TransactionManager transactionManager;
    private final SupplierManager supplierManager;
    private final GraphNetwork graphNetwork;
    private final SalesAnalytics salesAnalytics;
    private final SortingService sortingService;
    private final OptimizationService optimizationService;
    private final ComprehensiveReportService reportService;

    /**
     * Creates the application with a scanner and inventory manager.
     */
    public RetailMindApp() {
        scanner = new Scanner(System.in);
        inventoryManager = new InventoryManager();
        customerManager = new CustomerManager();
        supplierManager = new SupplierManager();
        graphNetwork = new GraphNetwork();
        salesAnalytics = new SalesAnalytics();
        transactionManager = new TransactionManager(inventoryManager, customerManager, salesAnalytics);
        sortingService = new SortingService(inventoryManager, transactionManager);
        optimizationService = new OptimizationService(inventoryManager);
        reportService = new ComprehensiveReportService(inventoryManager, transactionManager, customerManager, supplierManager, graphNetwork, salesAnalytics, sortingService, optimizationService);
    }

    /**
     * Starts the RetailMind application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        RetailMindApp app = new RetailMindApp();
        app.run();
    }

    /**
     * Runs the main menu until the user exits.
     */
    public void run() {
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = readPositiveInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    inventoryManager.displayProductsSorted();
                    break;
                case 3:
                    searchProduct();
                    break;
                case 4:
                    updateProduct();
                    break;
                case 5:
                    deleteProduct();
                    break;
                case 6:
                    inventoryManager.displayCategories();
                    break;
                case 7:
                    searchProductUsingBTree();
                    break;
                case 8:
                    searchProductsByPriceRange();
                    break;
                case 9:
                    showSalesAnalytics();
                    break;
                case 10:
                    showCumulativeRevenue();
                    break;
                case 11:
                    registerCustomer();
                    break;
                case 12:
                    customerManager.displayCustomers();
                    break;
                case 13:
                    purchaseProduct();
                    break;
                case 14:
                    transactionManager.displayTransactions();
                    break;
                case 15:
                    supplierNetworkMenu();
                    break;
                case 16:
                    sortingMenu();
                    break;
                case 17:
                    optimizationMenu();
                    break;
                case 18:
                    reportMenu();
                    break;
                case 19:
                    running = false;
                    System.out.println("Thank you for using RetailMind.");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number from 1 to 19.");
            }
        }

        scanner.close();
    }

    /**
     * Displays the main menu.
     */
    private void displayMenu() {
        // System.out.println("\n==============================");
        // System.out.println("RetailMind Main Menu");
        // System.out.println("====================");
        System.out.println("""
 ███████████             █████               ███  ████  ██████   ██████  ███                 █████
░░███░░░░░███           ░░███               ░░░  ░░███ ░░██████ ██████  ░░░                 ░░███
 ░███    ░███   ██████  ███████    ██████   ████  ░███  ░███░█████░███  ████  ████████    ███████
 ░██████████   ███░░███░░░███░    ░░░░░███ ░░███  ░███  ░███░░███ ░███ ░░███ ░░███░░███  ███░░███
 ░███░░░░░███ ░███████   ░███      ███████  ░███  ░███  ░███ ░░░  ░███  ░███  ░███ ░███ ░███ ░███
 ░███    ░███ ░███░░░    ░███ ███ ███░░███  ░███  ░███  ░███      ░███  ░███  ░███ ░███ ░███ ░███
 █████   █████░░██████   ░░█████ ░░████████ █████ █████ █████     █████ █████ ████ █████░░████████
░░░░░   ░░░░░  ░░░░░░     ░░░░░   ░░░░░░░░ ░░░░░ ░░░░░ ░░░░░     ░░░░░ ░░░░░ ░░░░ ░░░░░  ░░░░░░░░


 ██████   ██████            ███                ██████   ██████
░░██████ ██████            ░░░                ░░██████ ██████
 ░███░█████░███   ██████   ████  ████████      ░███░█████░███   ██████  ████████   █████ ████
 ░███░░███ ░███  ░░░░░███ ░░███ ░░███░░███     ░███░░███ ░███  ███░░███░░███░░███ ░░███ ░███
 ░███ ░░░  ░███   ███████  ░███  ░███ ░███     ░███ ░░░  ░███ ░███████  ░███ ░███  ░███ ░███
 ░███      ░███  ███░░███  ░███  ░███ ░███     ░███      ░███ ░███░░░   ░███ ░███  ░███ ░███
 █████     █████░░████████ █████ ████ █████    █████     █████░░██████  ████ █████ ░░████████
░░░░░     ░░░░░  ░░░░░░░░ ░░░░░ ░░░░ ░░░░░    ░░░░░     ░░░░░  ░░░░░░  ░░░░ ░░░░░   ░░░░░░░░
""");
        System.out.println("1. Add Product");
        System.out.println("2. Display Products (Sorted)");
        System.out.println("3. Search Product (AVL)");
        System.out.println("4. Update Product");
        System.out.println("5. Delete Product");
        System.out.println("6. Display Categories");
        System.out.println("7. Search Product using B-Tree");
        System.out.println("8. Search Products by Price Range (B+ Tree)");
        System.out.println("9. Sales Analytics (Segment Tree)");
        System.out.println("10. Cumulative Revenue (Fenwick Tree)");
        System.out.println("11. Register Customer");
        System.out.println("12. Display Customers");
        System.out.println("13. Purchase Product");
        System.out.println("14. Display Transactions");
        System.out.println("15. Supplier Network");
        System.out.println("16. Sorting & Ranking Menu");
        System.out.println("17. Business Optimization Menu");
        System.out.println("18. Report Generation Menu");
        System.out.println("19. Exit");
    }

    /**
     * Reads product details and adds a new product.
     */
    private void addProduct() {
        System.out.println("\nAdd Product");
        int productId = readPositiveInt("Enter product ID: ");

        if (inventoryManager.searchProduct(productId) != null) {
            System.out.println("A product with this ID already exists.");
            return;
        }

        String productName = readRequiredText("Enter product name: ");
        String category = readRequiredText("Enter category: ");
        double price = readPositiveDouble("Enter price: ");
        int stockQuantity = readPositiveInt("Enter stock quantity: ");
        String barcode = readRequiredText("Enter barcode: ");

        Product product = new Product(productId, productName, category, price, stockQuantity, barcode);
        if (inventoryManager.addProduct(product)) {
            System.out.println("Product added successfully and saved to products.txt.");
        } else {
            System.out.println("Product could not be added. Please check the product details.");
        }
    }

    /**
     * Searches and displays a product by ID.
     */
    private void searchProduct() {
        System.out.println("\nSearch Product");
        int productId = readPositiveInt("Enter product ID: ");
        Product product = inventoryManager.searchProduct(productId);

        if (product == null) {
            System.out.println("Product not found.");
        } else {
            System.out.println(product);
        }
    }

    /**
     * Reads updated product details and updates an existing product.
     */
    private void updateProduct() {
        System.out.println("\nUpdate Product");
        int productId = readPositiveInt("Enter product ID to update: ");

        if (inventoryManager.searchProduct(productId) == null) {
            System.out.println("Product not found.");
            return;
        }

        String productName = readRequiredText("Enter new product name: ");
        String category = readRequiredText("Enter new category: ");
        double price = readPositiveDouble("Enter new price: ");
        int stockQuantity = readPositiveInt("Enter new stock quantity: ");
        String barcode = readRequiredText("Enter new barcode: ");

        boolean updated = inventoryManager.updateProduct(productId, productName, category, price, stockQuantity, barcode);
        if (updated) {
            System.out.println("Product updated successfully and saved to products.txt.");
        } else {
            System.out.println("Product could not be updated.");
        }
    }

    /**
     * Deletes an existing product by ID.
     */
    private void deleteProduct() {
        System.out.println("\nDelete Product");
        int productId = readPositiveInt("Enter product ID to delete: ");

        if (inventoryManager.deleteProduct(productId)) {
            System.out.println("Product deleted successfully and removed from products.txt.");
        } else {
            System.out.println("Product not found.");
        }
    }

    /**
     * Searches for a product using the B-Tree index.
     */
    private void searchProductUsingBTree() {
        System.out.println("\nSearching using B-Tree Index...");
        int productId = readPositiveInt("Enter product ID: ");
        Product product = inventoryManager.searchUsingBTree(productId);

        if (product == null) {
            System.out.println("Product not found in B-Tree index.");
        } else {
            System.out.println(product);
        }
    }

    /**
     * Searches products by price range using the B+ Tree index.
     */
    private void searchProductsByPriceRange() {
        System.out.println("\nSearch Products by Price Range");
        double minPrice = readPositiveDouble("Enter minimum price: ");
        double maxPrice = readPositiveDouble("Enter maximum price: ");

        if (minPrice > maxPrice) {
            System.out.println("Invalid price range. Minimum price cannot be greater than maximum price.");
            return;
        }

        List<Product> products = inventoryManager.searchProductsByPriceRange(minPrice, maxPrice);
        if (products.isEmpty()) {
            System.out.println("No products found in the selected price range.");
            return;
        }

        displayProductTable(products);
    }

    /**
     * Runs a Segment Tree revenue query and saves the report.
     */
    private void showSalesAnalytics() {
        System.out.println("\nSales Analytics");
        int startDay = readPositiveInt("Enter Start Day: ");
        int endDay = readPositiveInt("Enter End Day: ");

        try {
            double totalRevenue = salesAnalytics.getRevenueBetweenDays(startDay, endDay);
            System.out.printf("Total Sales Between Day %d and Day %d: %.2f%n", startDay, endDay, totalRevenue);
            salesAnalytics.generateAnalyticsReport(startDay, endDay, totalRevenue);
            System.out.println("Analytics report saved to reports/analytics_report.txt.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Invalid day query. " + exception.getMessage());
        }
    }

    /**
     * Runs a Fenwick Tree cumulative revenue query and saves the report.
     */
    private void showCumulativeRevenue() {
        System.out.println("\nCumulative Revenue");
        int day = readPositiveInt("Enter Day: ");

        try {
            double cumulativeRevenue = salesAnalytics.getCumulativeRevenue(day);
            System.out.printf("Revenue till Day %d: %.2f%n", day, cumulativeRevenue);
            salesAnalytics.generateRevenueReport(day, cumulativeRevenue);
            System.out.println("Revenue report saved to reports/revenue_report.txt.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Invalid day query. " + exception.getMessage());
        }
    }

    /**
     * Displays products in a formatted table.
     *
     * @param products products to display
     */
    private void displayProductTable(List<Product> products) {
        System.out.println("\n--------------------------------------------------------------");
        System.out.printf("%-6s %-12s %-14s %-10s %-8s %-10s%n", "## ID", "Name", "Category", "Price", "Stock",
                "Barcode");
        System.out.println("--------------------------------------------------------------");
        for (Product product : products) {
            System.out.printf("%-6d %-12s %-14s %-10.2f %-8d %-10s%n",
                    product.getProductId(),
                    product.getProductName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    product.getBarcode());
        }
    }

    /**
     * Reads a positive integer from the user.
     *
     * @param prompt text shown to the user
     * @return validated positive integer
     */
    private int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("Value cannot be negative.");
                    continue;
                }
                return value;
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Reads a positive decimal number from the user.
     *
     * @param prompt text shown to the user
     * @return validated positive decimal number
     */
    private double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                double value = Double.parseDouble(input);
                if (value < 0) {
                    System.out.println("Value cannot be negative.");
                    continue;
                }
                return value;
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid decimal number.");
            }
        }
    }

    /**
     * Reads required text from the user.
     *
     * @param prompt text shown to the user
     * @return non-empty text
     */
    private String readRequiredText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    /**
     * Registers a new customer.
     */
    private void registerCustomer() {
        System.out.println("\nRegister Customer");
        int customerId = readPositiveInt("Enter customer ID: ");

        if (customerManager.searchCustomer(customerId) != null) {
            System.out.println("A customer with this ID already exists.");
            return;
        }

        String customerName = readRequiredText("Enter customer name: ");
        String contact = readRequiredText("Enter contact number: ");

        Customer customer = new Customer(customerId, customerName, contact);
        if (customerManager.registerCustomer(customer)) {
            System.out.println("Customer registered successfully and saved to customers.txt.");
        } else {
            System.out.println("Customer could not be registered. Please check the customer details.");
        }
    }

    /**
     * Processes a product purchase.
     */
    private void purchaseProduct() {
        System.out.println("\nPurchase Product");
        int customerId = readPositiveInt("Enter customer ID: ");
        int productId = readPositiveInt("Enter product ID: ");
        int quantity = readPositiveInt("Enter quantity: ");

        System.out.println("\nProcessing purchase...");
        boolean success = transactionManager.purchaseProduct(customerId, productId, quantity);

        if (!success) {
            System.out.println("Purchase failed. Please check the details and try again.");
        }
    }

    /**
     * Displays the supplier network submenu.
     */
    private void supplierNetworkMenu() {
        boolean inSubMenu = true;

        while (inSubMenu) {
            displaySupplierNetworkMenu();
            int choice = readPositiveInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addSupplier();
                    break;
                case 2:
                    addSupplierToNetwork();
                    break;
                case 3:
                    supplierManager.displaySuppliers();
                    break;
                case 4:
                    connectSuppliers();
                    break;
                case 5:
                    graphNetwork.displayNetwork();
                    break;
                case 6:
                    bfsTraversal();
                    break;
                case 7:
                    dfsTraversal();
                    break;
                case 8:
                    checkConnectivity();
                    break;
                case 9:
                    detectCycles();
                    break;
                case 10:
                    generateMST();
                    break;
                case 11:
                    generateNetworkReport();
                    break;
                case 12:
                    routeOptimizationMenu();
                    break;
                case 13:
                    inSubMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number from 1 to 13.");
            }
        }
    }

    /**
     * Displays the supplier network submenu.
     */
    private void displaySupplierNetworkMenu() {
        System.out.println("\n==============================");
        System.out.println("Supplier Network Menu");
        System.out.println("====================");

        System.out.println("1. Add Supplier (Database)");
        System.out.println("2. Add Supplier (Network)");
        System.out.println("3. Display Suppliers");
        System.out.println("4. Connect Suppliers");
        System.out.println("5. Display Supplier Network");
        System.out.println("6. BFS Traversal");
        System.out.println("7. DFS Traversal");
        System.out.println("8. Check Connectivity");
        System.out.println("9. Detect Cycles");
        System.out.println("10. Generate MST");
        System.out.println("11. Generate Network Report");
        System.out.println("12. Route Optimization");
        System.out.println("13. Back to Main Menu");
    }

    /**
     * Adds a supplier to the supplier database.
     */
    private void addSupplier() {
        System.out.println("\nAdd Supplier (Database)");
        int supplierId = readPositiveInt("Enter supplier ID: ");

        if (supplierManager.searchSupplier(supplierId) != null) {
            System.out.println("A supplier with this ID already exists.");
            return;
        }

        String supplierName = readRequiredText("Enter supplier name: ");
        String location = readRequiredText("Enter location: ");

        retailmind.model.Supplier supplier = new retailmind.model.Supplier(supplierId, supplierName, location);
        if (supplierManager.addSupplier(supplier)) {
            System.out.println("Supplier added successfully and saved to suppliers.txt.");
        } else {
            System.out.println("Supplier could not be added. Please check the supplier details.");
        }
    }

    /**
     * Adds a supplier to the network graph.
     */
    private void addSupplierToNetwork() {
        System.out.println("\nAdd Supplier (Network)");
        String supplierName = readRequiredText("Enter supplier name: ");

        if (graphNetwork.addSupplier(supplierName)) {
            System.out.println("Supplier added to network successfully.");
        } else {
            System.out.println("Supplier could not be added to network.");
        }
    }

    /**
     * Connects two suppliers in the network.
     */
    private void connectSuppliers() {
        System.out.println("\nConnect Suppliers");
        String source = readRequiredText("Enter source supplier: ");
        String destination = readRequiredText("Enter destination supplier: ");
        double cost = readPositiveDouble("Enter connection cost: ");

        if (graphNetwork.connectSuppliers(source, destination, cost)) {
            System.out.println("Suppliers connected successfully.");
        } else {
            System.out.println("Connection could not be established.");
        }
    }

    /**
     * Performs BFS traversal.
     */
    private void bfsTraversal() {
        System.out.println("\nBFS Traversal");
        String start = readRequiredText("Enter starting supplier: ");

        List<String> traversal = graphNetwork.breadthFirstTraversal(start);
        if (!traversal.isEmpty()) {
            System.out.println("BFS Traversal Order:");
            for (String supplier : traversal) {
                System.out.println(supplier);
            }
        }
    }

    /**
     * Performs DFS traversal.
     */
    private void dfsTraversal() {
        System.out.println("\nDFS Traversal");
        String start = readRequiredText("Enter starting supplier: ");

        List<String> traversal = graphNetwork.depthFirstTraversal(start);
        if (!traversal.isEmpty()) {
            System.out.println("DFS Traversal Order:");
            for (String supplier : traversal) {
                System.out.println(supplier);
            }
        }
    }

    /**
     * Checks network connectivity.
     */
    private void checkConnectivity() {
        System.out.println("\nCheck Connectivity");
        String start = readRequiredText("Enter starting supplier: ");

        boolean connected = graphNetwork.isConnected(start);
        if (connected) {
            System.out.println("The network is connected from " + start + ".");
        } else {
            System.out.println("The network is NOT connected from " + start + ".");
        }
    }

    /**
     * Detects cycles in the network.
     */
    private void detectCycles() {
        System.out.println("\nDetect Cycles");
        boolean hasCycle = graphNetwork.detectCycle();
        if (hasCycle) {
            System.out.println("Cycle Detected in the network.");
        } else {
            System.out.println("No Cycle Found in the network.");
        }
    }

    /**
     * Generates and displays MST.
     */
    private void generateMST() {
        System.out.println("\nGenerate Minimum Spanning Tree");
        GraphNetwork.MSTResult mstResult = graphNetwork.generateMST();

        if (mstResult.edges.isEmpty()) {
            System.out.println("No MST could be generated.");
            return;
        }

        System.out.println("Minimum Cost Supplier Network:");
        for (retailmind.service.GraphNetwork.MSTEdge edge : mstResult.edges) {
            System.out.println(edge.source + " -> " + edge.destination + " (Cost: " + edge.cost + ")");
        }
        System.out.println("Total Cost = " + String.format("%.2f", mstResult.totalCost));
    }

    /**
     * Generates network analysis report.
     */
    private void generateNetworkReport() {
        System.out.println("\nGenerate Network Report");
        graphNetwork.generateNetworkReport();
    }

    /**
     * Displays the route optimization submenu.
     */
    private void routeOptimizationMenu() {
        boolean inSubMenu = true;

        while (inSubMenu) {
            displayRouteOptimizationMenu();
            int choice = readPositiveInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    fastestRoute();
                    break;
                case 2:
                    cheapestRoutes();
                    break;
                case 3:
                    allPairsAnalysis();
                    break;
                case 4:
                    deliveryScheduling();
                    break;
                case 5:
                    generateRouteReport();
                    break;
                case 6:
                    inSubMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number from 1 to 6.");
            }
        }
    }

    /**
     * Displays the route optimization submenu.
     */
    private void displayRouteOptimizationMenu() {
        System.out.println("\n==============================");
        System.out.println("Route Optimization Menu");
        System.out.println("====================");

        System.out.println("1. Fastest Route (Dijkstra)");
        System.out.println("2. Cheapest Routes (Bellman-Ford)");
        System.out.println("3. All-Pairs Analysis (Floyd-Warshall)");
        System.out.println("4. Delivery Scheduling (Topological Sort)");
        System.out.println("5. Generate Route Report");
        System.out.println("6. Back to Supplier Network");
    }

    /**
     * Finds fastest route using Dijkstra's algorithm.
     */
    private void fastestRoute() {
        System.out.println("\nFastest Route (Dijkstra)");
        String source = readRequiredText("Enter source supplier: ");
        String destination = readRequiredText("Enter destination supplier: ");

        retailmind.service.GraphNetwork.PathResult result = graphNetwork.dijkstra(source, destination);

        if (result.path.isEmpty() || result.totalCost == Double.POSITIVE_INFINITY) {
            System.out.println("No path found between " + source + " and " + destination + ".");
            return;
        }

        System.out.println("\n====================================");
        System.out.println("Fastest Route");
        System.out.println("=============");

        for (int i = 0; i < result.path.size(); i++) {
            System.out.println(result.path.get(i));
            if (i < result.path.size() - 1) {
                System.out.println("->");
            }
        }

        System.out.println("\nTotal Cost: " + String.format("%.2f", result.totalCost));
        System.out.println("====================================");
    }

    /**
     * Finds cheapest routes using Bellman-Ford algorithm.
     */
    private void cheapestRoutes() {
        System.out.println("\nCheapest Routes (Bellman-Ford)");
        String source = readRequiredText("Enter source supplier: ");

        Map<String, Double> distances = graphNetwork.bellmanFord(source);

        if (distances == null) {
            System.out.println("Negative weight cycle detected. Cannot compute shortest paths.");
            return;
        }

        System.out.println("\nShortest distances from " + source + ":");
        System.out.println("====================================");

        for (Map.Entry<String, Double> entry : distances.entrySet()) {
            if (entry.getValue() == Double.POSITIVE_INFINITY) {
                System.out.println(entry.getKey() + ": Unreachable");
            } else {
                System.out.println(entry.getKey() + ": " + String.format("%.2f", entry.getValue()));
            }
        }
        System.out.println("====================================");
    }

    /**
     * Performs all-pairs analysis using Floyd-Warshall algorithm.
     */
    private void allPairsAnalysis() {
        System.out.println("\nAll-Pairs Analysis (Floyd-Warshall)");
        graphNetwork.generateShortestPathReport();
    }

    /**
     * Generates delivery schedule using topological sort.
     */
    private void deliveryScheduling() {
        System.out.println("\nDelivery Scheduling (Topological Sort)");
        graphNetwork.generateDeliverySchedule();
    }

    /**
     * Generates route report.
     */
    private void generateRouteReport() {
        System.out.println("\nGenerate Route Report");
        graphNetwork.generateShortestPathReport();
        graphNetwork.generateDeliverySchedule();
    }

    /**
     * Displays the sorting and ranking submenu.
     */
    private void sortingMenu() {
        boolean inSubMenu = true;

        while (inSubMenu) {
            System.out.println("\n==============================");
            System.out.println("Sorting & Ranking Menu");
            System.out.println("======================");
            System.out.println("31. Sort Products by Revenue (Merge Sort)");
            System.out.println("32. Sort Products by Popularity (Quick Sort)");
            System.out.println("33. Display Top Selling Products (Heap Sort)");
            System.out.println("34. Sort Products by Stock Quantity (Counting Sort)");
            System.out.println("35. Sort Products by Barcode (Radix Sort)");
            System.out.println("36. Generate Sorted Product Report");
            System.out.println("37. Generate Top Selling Report");
            System.out.println("38. Compare Sorting Algorithms");
            System.out.println("39. Back");
            
            int choice = readPositiveInt("Enter your choice: ");

            switch (choice) {
                case 31:
                    sortingService.sortByRevenue();
                    break;
                case 32:
                    sortingService.sortByPopularity();
                    break;
                case 33:
                    int n = readPositiveInt("Enter Top N value: ");
                    sortingService.displayTopSellingProducts(n);
                    break;
                case 34:
                    sortingService.sortByStock();
                    break;
                case 35:
                    sortingService.sortByBarcode();
                    break;
                case 36:
                    sortingService.generateSortedProductReport(sortingService.loadProductStatistics(), "No Sort (Current State)");
                    break;
                case 37:
                    int tn = readPositiveInt("Enter Top N value for report: ");
                    sortingService.generateTopSellingReport(retailmind.sorting.HeapSorter.getTopSellingProducts(sortingService.loadProductStatistics(), tn));
                    break;
                case 38:
                    sortingService.compareAlgorithms();
                    break;
                case 39:
                    inSubMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number from 31 to 39.");
            }
        }
    }

    /**
     * Displays the business optimization submenu.
     */
    private void optimizationMenu() {
        boolean inSubMenu = true;

        while (inSubMenu) {
            System.out.println("\n==============================");
            System.out.println("Business Optimization Menu");
            System.out.println("==========================");
            System.out.println("40. Schedule Promotions (Activity Selection)");
            System.out.println("41. Optimize Shelf Space (Fractional Knapsack)");
            System.out.println("42. Optimize Inventory Stock (0/1 Knapsack)");
            System.out.println("43. Analyze Growth Trend (LIS)");
            System.out.println("44. Generate Optimization Reports");
            System.out.println("45. Back");
            
            int choice = readPositiveInt("Enter your choice: ");

            switch (choice) {
                case 40:
                    optimizationService.schedulePromotions();
                    break;
                case 41:
                    double fractionCap = readPositiveDouble("Enter shelf capacity: ");
                    optimizationService.optimizeShelfSpace(fractionCap);
                    break;
                case 42:
                    int zeroOneCap = readPositiveInt("Enter warehouse capacity: ");
                    optimizationService.optimizeInventoryStock(zeroOneCap);
                    break;
                case 43:
                    optimizationService.analyzeGrowthTrend();
                    break;
                case 44:
                    System.out.println("Generating all optimization reports...");
                    optimizationService.schedulePromotions();
                    optimizationService.optimizeShelfSpace(100);
                    optimizationService.optimizeInventoryStock(100);
                    optimizationService.analyzeGrowthTrend();
                    break;
                case 45:
                    inSubMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number from 40 to 45.");
            }
        }
    }

    /**
     * Displays the report generation submenu.
     */
    private void reportMenu() {
        boolean inSubMenu = true;

        while (inSubMenu) {
            System.out.println("\n==============================");
            System.out.println("Report Generation Menu");
            System.out.println("======================");
            System.out.println("46. Generate Inventory Report");
            System.out.println("47. Generate Sales Report");
            System.out.println("48. Generate Supplier Report");
            System.out.println("49. Generate Revenue Report");
            System.out.println("50. Generate Analytics Report");
            System.out.println("51. Generate Final Summary Report");
            System.out.println("52. Generate All Reports");
            System.out.println("53. Back");
            
            int choice = readPositiveInt("Enter your choice: ");

            switch (choice) {
                case 46:
                    reportService.generateInventoryReport();
                    break;
                case 47:
                    reportService.generateSalesReport();
                    break;
                case 48:
                    reportService.generateSupplierReport();
                    break;
                case 49:
                    reportService.generateRevenueReport();
                    break;
                case 50:
                    reportService.generateAnalyticsReport();
                    break;
                case 51:
                    reportService.generateFinalSummaryReport();
                    break;
                case 52:
                    reportService.generateAllReports();
                    break;
                case 53:
                    inSubMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number from 46 to 53.");
            }
        }
    }
}
