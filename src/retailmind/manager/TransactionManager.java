package retailmind.manager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import retailmind.model.Customer;
import retailmind.model.Product;
import retailmind.model.Transaction;
import retailmind.service.SalesAnalytics;
import retailmind.util.FileManager;

/**
 * Manages transaction operations including purchase processing and billing.
 */
public class TransactionManager {
    private final ArrayList<Transaction> transactions;
    private final InventoryManager inventoryManager;
    private final CustomerManager customerManager;
    private final SalesAnalytics salesAnalytics;
    private int transactionCounter;

    /**
     * Creates a transaction manager and loads transactions from file.
     *
     * @param inventoryManager inventory manager for product operations
     * @param customerManager customer manager for customer operations
     * @param salesAnalytics sales analytics for revenue updates
     */
    public TransactionManager(InventoryManager inventoryManager, CustomerManager customerManager,
            SalesAnalytics salesAnalytics) {
        FileManager.createDataFiles();
        this.inventoryManager = inventoryManager;
        this.customerManager = customerManager;
        this.salesAnalytics = salesAnalytics;
        this.transactions = FileManager.loadTransactions();
        this.transactionCounter = getNextTransactionNumber();
    }

    /**
     * Processes a product purchase with validation.
     *
     * @param customerId customer ID
     * @param productId product ID
     * @param quantity quantity to purchase
     * @return true if purchase was successful, otherwise false
     */
    public boolean purchaseProduct(int customerId, int productId, int quantity) {
        if (quantity <= 0) {
            System.out.println("Invalid quantity. Quantity must be greater than 0.");
            return false;
        }

        Customer customer = customerManager.searchCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer not found. Please check the customer ID.");
            return false;
        }

        Product product = inventoryManager.searchProduct(productId);
        if (product == null) {
            System.out.println("Product not found. Please check the product ID.");
            return false;
        }

        if (product.getStockQuantity() < quantity) {
            System.out.println("Insufficient Stock Available. Available: " + product.getStockQuantity());
            return false;
        }

        double totalAmount = product.getPrice() * quantity;
        String transactionId = generateTransactionId();
        String transactionDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        Transaction transaction = new Transaction(transactionId, customerId, productId, quantity, totalAmount,
                transactionDate);

        saveTransaction(transaction);
        updateProductStock(product, quantity);
        updateRevenue(totalAmount);

        generateBill(transaction, customer, product);
        return true;
    }

    /**
     * Generates and displays a formatted bill.
     *
     * @param transaction transaction details
     * @param customer customer details
     * @param product product details
     */
    public void generateBill(Transaction transaction, Customer customer, Product product) {
        System.out.println("\n==================================");
        System.out.println("RetailMind Purchase Bill");
        System.out.println("========================");
        System.out.println("Transaction ID: " + transaction.getTransactionId());
        System.out.println("Date: " + transaction.getTransactionDate());
        System.out.println("Customer: " + customer.getCustomerName() + " (ID: " + customer.getCustomerId() + ")");
        System.out.println("Product: " + product.getProductName() + " (ID: " + product.getProductId() + ")");
        System.out.println("Price: " + String.format("%.2f", product.getPrice()));
        System.out.println("Quantity: " + transaction.getQuantity());
        System.out.println("Subtotal: " + String.format("%.2f", transaction.getTotalAmount()));
        System.out.println("==================================");
        System.out.println("Total Amount: " + String.format("%.2f", transaction.getTotalAmount()));
        System.out.println("==================================");
    }

    /**
     * Saves a transaction to the list and file.
     *
     * @param transaction transaction to save
     */
    public void saveTransaction(Transaction transaction) {
        transactions.add(transaction);
        FileManager.appendTransaction(transaction);
    }

    /**
     * Displays all transactions in a formatted table.
     */
    public void displayTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions available.");
            return;
        }

        System.out.println("\n--------------------------------------------------------------------------");
        System.out.printf("%-15s %-10s %-10s %-6s %-10s %-12s%n", "## Transaction ID", "Customer", "Product", "Qty",
                "Total", "Date");
        System.out.println("--------------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            System.out.printf("%-15s %-10d %-10d %-6d %-10.2f %-12s%n",
                    transaction.getTransactionId(),
                    transaction.getCustomerId(),
                    transaction.getProductId(),
                    transaction.getQuantity(),
                    transaction.getTotalAmount(),
                    transaction.getTransactionDate());
        }
    }

    /**
     * Generates the next transaction ID in the format T1001, T1002, etc.
     *
     * @return next transaction ID
     */
    private String generateTransactionId() {
        transactionCounter++;
        return "T" + transactionCounter;
    }

    /**
     * Determines the next transaction number based on existing transactions.
     *
     * @return next transaction number
     */
    private int getNextTransactionNumber() {
        int maxNumber = 1000;
        for (Transaction transaction : transactions) {
            String id = transaction.getTransactionId();
            if (id.startsWith("T")) {
                try {
                    int number = Integer.parseInt(id.substring(1));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid transaction IDs
                }
            }
        }
        return maxNumber;
    }

    /**
     * Updates product stock after purchase.
     *
     * @param product product to update
     * @param quantity quantity purchased
     */
    private void updateProductStock(Product product, int quantity) {
        int newStock = product.getStockQuantity() - quantity;
        inventoryManager.updateProduct(product.getProductId(), product.getProductName(), product.getCategory(),
                product.getPrice(), newStock, product.getBarcode());
    }

    /**
     * Updates sales analytics with the purchase amount.
     *
     * @param amount purchase amount
     */
    private void updateRevenue(double amount) {
        salesAnalytics.loadSalesData();
        int currentDay = salesAnalytics.getAvailableDayCount();
        if (currentDay == 0) {
            currentDay = 1;
        }
        salesAnalytics.updateRevenue(currentDay, amount);
    }

    /**
     * Gets a copy of all transactions.
     *
     * @return transaction list copy
     */
    public ArrayList<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}
