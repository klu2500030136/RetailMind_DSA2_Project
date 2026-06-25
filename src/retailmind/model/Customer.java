package retailmind.model;

/**
 * Represents a customer in the RetailMind system.
 */
public class Customer {
    private int customerId;
    private String customerName;
    private String contact;

    /**
     * Creates an empty customer object.
     */
    public Customer() {
    }

    /**
     * Creates a customer object with all customer details.
     *
     * @param customerId unique customer ID
     * @param customerName customer name
     * @param contact customer contact information
     */
    public Customer(int customerId, String customerName, String contact) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.contact = contact;
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
     * Gets the customer name.
     *
     * @return customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the customer name.
     *
     * @param customerName customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets the customer contact information.
     *
     * @return customer contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the customer contact information.
     *
     * @param contact customer contact
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Converts the customer into comma-separated file format.
     *
     * @return customer data for file storage
     */
    public String toFileString() {
        return customerId + "," + customerName + "," + contact;
    }

    /**
     * Returns a readable customer summary.
     *
     * @return customer details
     */
    @Override
    public String toString() {
        return "Customer ID: " + customerId + ", Name: " + customerName + ", Contact: " + contact;
    }
}
