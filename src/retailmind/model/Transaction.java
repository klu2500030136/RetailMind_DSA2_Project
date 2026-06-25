package retailmind.model;

/**
 * Represents a sales transaction in the RetailMind system.
 */
public class Transaction {
    private String transactionId;
    private int customerId;
    private int productId;
    private int quantity;
    private double totalAmount;
    private String transactionDate;

    /**
     * Creates an empty transaction object.
     */
    public Transaction() {
    }

    /**
     * Creates a transaction object with all transaction details.
     *
     * @param transactionId unique transaction ID
     * @param customerId customer ID linked to the transaction
     * @param productId product ID linked to the transaction
     * @param quantity purchased quantity
     * @param totalAmount total bill amount
     * @param transactionDate transaction date
     */
    public Transaction(String transactionId, int customerId, int productId, int quantity, double totalAmount,
            String transactionDate) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.transactionDate = transactionDate;
    }

    /**
     * Gets the transaction ID.
     *
     * @return transaction ID
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the transaction ID.
     *
     * @param transactionId transaction ID
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Gets the customer ID.
     *
     * @return customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID.
     *
     * @param customerId customer ID
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the product ID.
     *
     * @return product ID
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Sets the product ID.
     *
     * @param productId product ID
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * Gets the purchased quantity.
     *
     * @return purchased quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the purchased quantity.
     *
     * @param quantity purchased quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the total transaction amount.
     *
     * @return total amount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Sets the total transaction amount.
     *
     * @param totalAmount total amount
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Gets the transaction date.
     *
     * @return transaction date
     */
    public String getTransactionDate() {
        return transactionDate;
    }

    /**
     * Sets the transaction date.
     *
     * @param transactionDate transaction date
     */
    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * Converts the transaction into comma-separated file format.
     *
     * @return transaction data for file storage
     */
    public String toFileString() {
        return transactionId + "," + customerId + "," + productId + "," + quantity + "," + totalAmount + ","
                + transactionDate;
    }

    /**
     * Returns a readable transaction summary.
     *
     * @return transaction details
     */
    @Override
    public String toString() {
        return "Transaction ID: " + transactionId
                + ", Customer ID: " + customerId
                + ", Product ID: " + productId
                + ", Quantity: " + quantity
                + ", Total Amount: " + totalAmount
                + ", Date: " + transactionDate;
    }
}
