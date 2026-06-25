package retailmind.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retailmind.model.Customer;
import retailmind.model.Edge;
import retailmind.model.Product;
import retailmind.model.Supplier;
import retailmind.model.Transaction;

/**
 * Handles file creation, loading, saving, and appending for RetailMind data.
 */
public class FileManager {
    private static final Path DATA_DIRECTORY = Paths.get("data");
    private static final Path PRODUCTS_FILE = DATA_DIRECTORY.resolve("products.txt");
    private static final Path CUSTOMERS_FILE = DATA_DIRECTORY.resolve("customers.txt");
    private static final Path SUPPLIERS_FILE = DATA_DIRECTORY.resolve("suppliers.txt");
    private static final Path TRANSACTIONS_FILE = DATA_DIRECTORY.resolve("transactions.txt");
    private static final Path SALES_FILE = DATA_DIRECTORY.resolve("sales.txt");
    private static final Path NETWORK_FILE = DATA_DIRECTORY.resolve("network.txt");

    /**
     * Prevents object creation because FileManager provides only static utility methods.
     */
    private FileManager() {
    }

    /**
     * Creates all required data files if they do not already exist.
     */
    public static void createDataFiles() {
        try {
            Files.createDirectories(DATA_DIRECTORY);
            createFileIfMissing(PRODUCTS_FILE);
            createFileIfMissing(CUSTOMERS_FILE);
            createFileIfMissing(SUPPLIERS_FILE);
            createFileIfMissing(TRANSACTIONS_FILE);
            createFileIfMissing(SALES_FILE);
            createFileIfMissing(NETWORK_FILE);
            insertSampleProductsIfEmpty();
            insertSampleSalesIfEmpty();
            insertSampleCustomersIfEmpty();
            insertSampleNetworkIfEmpty();
        } catch (IOException exception) {
            System.out.println("Unable to create data files: " + exception.getMessage());
        }
    }

    /**
     * Loads all products from products.txt.
     *
     * @return list of loaded products
     */
    public static ArrayList<Product> loadProducts() {
        createDataFiles();
        ArrayList<Product> products = new ArrayList<>();
        List<Integer> loadedProductIds = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(PRODUCTS_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product product = parseProduct(line);
                if (product != null && !loadedProductIds.contains(product.getProductId())) {
                    products.add(product);
                    loadedProductIds.add(product.getProductId());
                } else if (product != null) {
                    System.out.println("Skipping duplicate product ID: " + product.getProductId());
                }
            }
        } catch (IOException exception) {
            System.out.println("Unable to load products: " + exception.getMessage());
        }

        return products;
    }

    /**
     * Saves all products to products.txt.
     *
     * @param products products to save
     */
    public static void saveProducts(List<Product> products) {
        createDataFiles();

        try (BufferedWriter writer = Files.newBufferedWriter(PRODUCTS_FILE)) {
            for (Product product : products) {
                writer.write(product.toFileString());
                writer.newLine();
            }
        } catch (IOException exception) {
            System.out.println("Unable to save products: " + exception.getMessage());
        }
    }

    /**
     * Appends one product to products.txt.
     *
     * @param product product to append
     */
    public static void appendProduct(Product product) {
        createDataFiles();

        try (BufferedWriter writer = Files.newBufferedWriter(PRODUCTS_FILE, StandardOpenOption.APPEND)) {
            writer.write(product.toFileString());
            writer.newLine();
        } catch (IOException exception) {
            System.out.println("Unable to append product: " + exception.getMessage());
        }
    }

    /**
     * Creates one file when it is missing.
     *
     * @param filePath file path to create
     * @throws IOException when the file cannot be created
     */
    private static void createFileIfMissing(Path filePath) throws IOException {
        if (Files.notExists(filePath)) {
            Files.createFile(filePath);
        }
    }

    /**
     * Inserts starter products when products.txt is empty.
     *
     * @throws IOException when the sample data cannot be written
     */
    private static void insertSampleProductsIfEmpty() throws IOException {
        if (!isBlankFile(PRODUCTS_FILE)) {
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(PRODUCTS_FILE)) {
            writer.write("101,Laptop,Electronics,55000,10,BC101");
            writer.newLine();
            writer.write("102,Mouse,Accessories,500,50,BC102");
            writer.newLine();
            writer.write("103,Keyboard,Accessories,1200,25,BC103");
            writer.newLine();
        }
    }

    /**
     * Inserts starter sales records when sales.txt is empty.
     *
     * @throws IOException when sample sales data cannot be written
     */
    private static void insertSampleSalesIfEmpty() throws IOException {
        if (!isBlankFile(SALES_FILE)) {
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(SALES_FILE)) {
            writer.write("Day,Revenue");
            writer.newLine();
            writer.write("1,5000");
            writer.newLine();
            writer.write("2,6200");
            writer.newLine();
            writer.write("3,8000");
            writer.newLine();
            writer.write("4,7500");
            writer.newLine();
            writer.write("5,9200");
            writer.newLine();
            writer.write("6,6100");
            writer.newLine();
            writer.write("7,11000");
            writer.newLine();
            writer.write("8,9800");
            writer.newLine();
            writer.write("9,7300");
            writer.newLine();
            writer.write("10,8700");
            writer.newLine();
        }
    }

    /**
     * Inserts starter customers when customers.txt is empty.
     *
     * @throws IOException when sample customer data cannot be written
     */
    private static void insertSampleCustomersIfEmpty() throws IOException {
        if (!isBlankFile(CUSTOMERS_FILE)) {
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(CUSTOMERS_FILE)) {
            writer.write("1,John Doe,9876543210");
            writer.newLine();
            writer.write("2,Alice Smith,9123456780");
            writer.newLine();
        }
    }

    /**
     * Inserts starter network data when network.txt is empty.
     *
     * @throws IOException when sample network data cannot be written
     */
    private static void insertSampleNetworkIfEmpty() throws IOException {
        if (!isBlankFile(NETWORK_FILE)) {
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(NETWORK_FILE)) {
            writer.write("SupplierA,SupplierB,50");
            writer.newLine();
            writer.write("SupplierA,SupplierC,40");
            writer.newLine();
            writer.write("SupplierB,SupplierD,60");
            writer.newLine();
            writer.write("SupplierC,SupplierD,20");
            writer.newLine();
            writer.write("SupplierC,SupplierE,80");
            writer.newLine();
        }
    }

    /**
     * Loads all customers from customers.txt.
     *
     * @return list of loaded customers
     */
    public static ArrayList<Customer> loadCustomers() {
        createDataFiles();
        ArrayList<Customer> customers = new ArrayList<>();
        List<Integer> loadedCustomerIds = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(CUSTOMERS_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Customer customer = parseCustomer(line);
                if (customer != null && !loadedCustomerIds.contains(customer.getCustomerId())) {
                    customers.add(customer);
                    loadedCustomerIds.add(customer.getCustomerId());
                } else if (customer != null) {
                    System.out.println("Skipping duplicate customer ID: " + customer.getCustomerId());
                }
            }
        } catch (IOException exception) {
            System.out.println("Unable to load customers: " + exception.getMessage());
        }

        return customers;
    }

    /**
     * Saves all customers to customers.txt.
     *
     * @param customers customers to save
     */
    public static void saveCustomers(List<Customer> customers) {
        createDataFiles();

        try (BufferedWriter writer = Files.newBufferedWriter(CUSTOMERS_FILE)) {
            for (Customer customer : customers) {
                writer.write(customer.toFileString());
                writer.newLine();
            }
        } catch (IOException exception) {
            System.out.println("Unable to save customers: " + exception.getMessage());
        }
    }

    /**
     * Loads all transactions from transactions.txt.
     *
     * @return list of loaded transactions
     */
    public static ArrayList<Transaction> loadTransactions() {
        createDataFiles();
        ArrayList<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(TRANSACTIONS_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transaction transaction = parseTransaction(line);
                if (transaction != null) {
                    transactions.add(transaction);
                }
            }
        } catch (IOException exception) {
            System.out.println("Unable to load transactions: " + exception.getMessage());
        }

        return transactions;
    }

    /**
     * Loads all suppliers from suppliers.txt.
     *
     * @return list of loaded suppliers
     */
    public static ArrayList<Supplier> loadSuppliers() {
        createDataFiles();
        ArrayList<Supplier> suppliers = new ArrayList<>();
        List<Integer> loadedSupplierIds = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(SUPPLIERS_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Supplier supplier = parseSupplier(line);
                if (supplier != null && !loadedSupplierIds.contains(supplier.getSupplierId())) {
                    suppliers.add(supplier);
                    loadedSupplierIds.add(supplier.getSupplierId());
                } else if (supplier != null) {
                    System.out.println("Skipping duplicate supplier ID: " + supplier.getSupplierId());
                }
            }
        } catch (IOException exception) {
            System.out.println("Unable to load suppliers: " + exception.getMessage());
        }

        return suppliers;
    }

    /**
     * Saves all suppliers to suppliers.txt.
     *
     * @param suppliers suppliers to save
     */
    public static void saveSuppliers(List<Supplier> suppliers) {
        createDataFiles();

        try (BufferedWriter writer = Files.newBufferedWriter(SUPPLIERS_FILE)) {
            for (Supplier supplier : suppliers) {
                writer.write(supplier.toFileString());
                writer.newLine();
            }
        } catch (IOException exception) {
            System.out.println("Unable to save suppliers: " + exception.getMessage());
        }
    }

    /**
     * Saves all transactions to transactions.txt.
     *
     * @param transactions transactions to save
     */
    public static void saveTransactions(List<Transaction> transactions) {
        createDataFiles();

        try (BufferedWriter writer = Files.newBufferedWriter(TRANSACTIONS_FILE)) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.toFileString());
                writer.newLine();
            }
        } catch (IOException exception) {
            System.out.println("Unable to save transactions: " + exception.getMessage());
        }
    }

    /**
     * Appends one transaction to transactions.txt.
     *
     * @param transaction transaction to append
     */
    public static void appendTransaction(Transaction transaction) {
        createDataFiles();

        try (BufferedWriter writer = Files.newBufferedWriter(TRANSACTIONS_FILE, StandardOpenOption.APPEND)) {
            writer.write(transaction.toFileString());
            writer.newLine();
        } catch (IOException exception) {
            System.out.println("Unable to append transaction: " + exception.getMessage());
        }
    }

    /**
     * Checks whether a file is empty or contains only whitespace.
     *
     * @param filePath file to check
     * @return true when the file has no meaningful content
     * @throws IOException when the file cannot be read
     */
    private static boolean isBlankFile(Path filePath) throws IOException {
        return Files.notExists(filePath) || Files.readString(filePath).trim().isEmpty();
    }

    /**
     * Converts a file line into a Product object.
     *
     * @param line comma-separated product line
     * @return product object, or null if the line is invalid
     */
    private static Product parseProduct(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",");
        if (parts.length != 6) {
            System.out.println("Skipping malformed product record: " + line);
            return null;
        }

        try {
            int productId = Integer.parseInt(parts[0].trim());
            String productName = parts[1].trim();
            String category = parts[2].trim();
            double price = Double.parseDouble(parts[3].trim());
            int stockQuantity = Integer.parseInt(parts[4].trim());
            String barcode = parts[5].trim();

            if (productId < 0 || price < 0 || stockQuantity < 0 || productName.isEmpty() || category.isEmpty()
                    || barcode.isEmpty()) {
                System.out.println("Skipping invalid product record: " + line);
                return null;
            }

            return new Product(productId, productName, category, price, stockQuantity, barcode);
        } catch (NumberFormatException exception) {
            System.out.println("Skipping invalid product record: " + line);
            return null;
        }
    }

    /**
     * Converts a file line into a Customer object.
     *
     * @param line comma-separated customer line
     * @return customer object, or null if the line is invalid
     */
    private static Customer parseCustomer(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",");
        if (parts.length != 3) {
            System.out.println("Skipping malformed customer record: " + line);
            return null;
        }

        try {
            int customerId = Integer.parseInt(parts[0].trim());
            String customerName = parts[1].trim();
            String contact = parts[2].trim();

            if (customerId < 0 || customerName.isEmpty() || contact.isEmpty()) {
                System.out.println("Skipping invalid customer record: " + line);
                return null;
            }

            return new Customer(customerId, customerName, contact);
        } catch (NumberFormatException exception) {
            System.out.println("Skipping invalid customer record: " + line);
            return null;
        }
    }

    /**
     * Converts a file line into a Supplier object.
     *
     * @param line comma-separated supplier line
     * @return supplier object, or null if the line is invalid
     */
    private static Supplier parseSupplier(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",");
        if (parts.length != 3) {
            System.out.println("Skipping malformed supplier record: " + line);
            return null;
        }

        try {
            int supplierId = Integer.parseInt(parts[0].trim());
            String supplierName = parts[1].trim();
            String location = parts[2].trim();

            if (supplierId < 0 || supplierName.isEmpty() || location.isEmpty()) {
                System.out.println("Skipping invalid supplier record: " + line);
                return null;
            }

            return new Supplier(supplierId, supplierName, location);
        } catch (NumberFormatException exception) {
            System.out.println("Skipping invalid supplier record: " + line);
            return null;
        }
    }

    /**
     * Converts a file line into a Transaction object.
     *
     * @param line comma-separated transaction line
     * @return transaction object, or null if the line is invalid
     */
    private static Transaction parseTransaction(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",");
        if (parts.length != 6) {
            System.out.println("Skipping malformed transaction record: " + line);
            return null;
        }

        try {
            String transactionId = parts[0].trim();
            int customerId = Integer.parseInt(parts[1].trim());
            int productId = Integer.parseInt(parts[2].trim());
            int quantity = Integer.parseInt(parts[3].trim());
            double totalAmount = Double.parseDouble(parts[4].trim());
            String transactionDate = parts[5].trim();

            if (customerId < 0 || productId < 0 || quantity <= 0 || totalAmount < 0 || transactionDate.isEmpty()) {
                System.out.println("Skipping invalid transaction record: " + line);
                return null;
            }

            return new Transaction(transactionId, customerId, productId, quantity, totalAmount, transactionDate);
        } catch (NumberFormatException exception) {
            System.out.println("Skipping invalid transaction record: " + line);
            return null;
        }
    }

    /**
     * Loads supplier network from network.txt.
     *
     * @return adjacency list representation of the network
     */
    public static Map<String, List<Edge>> loadNetwork() {
        createDataFiles();
        Map<String, List<Edge>> network = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(NETWORK_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                EdgeConnection connection = parseNetworkLine(line);
                if (connection != null) {
                    network.computeIfAbsent(connection.source, k -> new ArrayList<>()).add(new Edge(connection.destination, connection.cost));
                }
            }
        } catch (IOException exception) {
            System.out.println("Unable to load network: " + exception.getMessage());
        }

        return network;
    }

    /**
     * Saves supplier network to network.txt.
     *
     * @param network adjacency list representation of the network
     */
    public static void saveNetwork(Map<String, List<Edge>> network) {
        createDataFiles();

        try (BufferedWriter writer = Files.newBufferedWriter(NETWORK_FILE)) {
            for (Map.Entry<String, List<Edge>> entry : network.entrySet()) {
                String source = entry.getKey();
                for (Edge edge : entry.getValue()) {
                    writer.write(source + "," + edge.getDestination() + "," + edge.getCost());
                    writer.newLine();
                }
            }
        } catch (IOException exception) {
            System.out.println("Unable to save network: " + exception.getMessage());
        }
    }

    /**
     * Parses a network line into source, destination, and cost.
     *
     * @param line comma-separated network line
     * @return edge connection, or null if invalid
     */
    private static EdgeConnection parseNetworkLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",");
        if (parts.length != 3) {
            System.out.println("Skipping malformed network record: " + line);
            return null;
        }

        try {
            String source = parts[0].trim();
            String destination = parts[1].trim();
            double cost = Double.parseDouble(parts[2].trim());

            if (source.isEmpty() || destination.isEmpty() || cost < 0) {
                System.out.println("Skipping invalid network record: " + line);
                return null;
            }

            return new EdgeConnection(source, destination, cost);
        } catch (NumberFormatException exception) {
            System.out.println("Skipping invalid network record: " + line);
            return null;
        }
    }

    /**
     * Small data holder for network line parsing.
     */
    private static class EdgeConnection {
        private final String source;
        private final String destination;
        private final double cost;

        EdgeConnection(String source, String destination, double cost) {
            this.source = source;
            this.destination = destination;
            this.cost = cost;
        }
    }
}
