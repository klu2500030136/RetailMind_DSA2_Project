package retailmind.manager;

import java.util.ArrayList;

import retailmind.model.Customer;
import retailmind.util.FileManager;

/**
 * Manages customer operations using an ArrayList.
 */
public class CustomerManager {
    private final ArrayList<Customer> customers;

    /**
     * Creates a customer manager and loads customers from file.
     */
    public CustomerManager() {
        FileManager.createDataFiles();
        customers = FileManager.loadCustomers();
    }

    /**
     * Registers a new customer.
     *
     * @param customer customer to register
     * @return true if the customer was registered, otherwise false
     */
    public boolean registerCustomer(Customer customer) {
        if (customer == null) {
            System.out.println("Customer cannot be null.");
            return false;
        }

        if (searchCustomer(customer.getCustomerId()) != null) {
            System.out.println("Customer ID already exists. Please use a different ID.");
            return false;
        }

        customers.add(customer);
        FileManager.saveCustomers(customers);
        return true;
    }

    /**
     * Searches for a customer by customer ID.
     *
     * @param customerId customer ID to search
     * @return matching customer, or null when not found
     */
    public Customer searchCustomer(int customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Displays all customers in a formatted table.
     */
    public void displayCustomers() {
        if (customers.isEmpty()) {
            System.out.println("No customers available.");
            return;
        }

        System.out.println("\n--------------------------------------------------");
        System.out.printf("%-6s %-20s %-15s%n", "## ID", "Name", "Contact");
        System.out.println("--------------------------------------------------");

        for (Customer customer : customers) {
            System.out.printf("%-6d %-20s %-15s%n",
                    customer.getCustomerId(),
                    customer.getCustomerName(),
                    customer.getContact());
        }
    }

    /**
     * Deletes a customer by customer ID.
     *
     * @param customerId customer ID to delete
     * @return true if the customer was deleted, otherwise false
     */
    public boolean deleteCustomer(int customerId) {
        Customer customer = searchCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return false;
        }

        customers.remove(customer);
        FileManager.saveCustomers(customers);
        return true;
    }

    /**
     * Updates an existing customer.
     *
     * @param customerId customer ID to update
     * @param customerName updated customer name
     * @param contact updated contact information
     * @return true if the customer was updated, otherwise false
     */
    public boolean updateCustomer(int customerId, String customerName, String contact) {
        Customer customer = searchCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return false;
        }

        customer.setCustomerName(customerName);
        customer.setContact(contact);
        FileManager.saveCustomers(customers);
        return true;
    }

    /**
     * Gets a copy of all customers.
     *
     * @return customer list copy
     */
    public ArrayList<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }
}
