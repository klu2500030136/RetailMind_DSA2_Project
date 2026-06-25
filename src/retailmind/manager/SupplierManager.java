package retailmind.manager;

import java.util.ArrayList;

import retailmind.model.Supplier;
import retailmind.util.FileManager;

/**
 * Manages supplier operations using an ArrayList.
 */
public class SupplierManager {
    private final ArrayList<Supplier> suppliers;

    /**
     * Creates a supplier manager and loads suppliers from file.
     */
    public SupplierManager() {
        FileManager.createDataFiles();
        suppliers = FileManager.loadSuppliers();
    }

    /**
     * Adds a new supplier.
     *
     * @param supplier supplier to add
     * @return true if the supplier was added, otherwise false
     */
    public boolean addSupplier(Supplier supplier) {
        if (supplier == null) {
            System.out.println("Supplier cannot be null.");
            return false;
        }

        if (searchSupplier(supplier.getSupplierId()) != null) {
            System.out.println("Supplier ID already exists. Please use a different ID.");
            return false;
        }

        suppliers.add(supplier);
        FileManager.saveSuppliers(suppliers);
        return true;
    }

    /**
     * Searches for a supplier by supplier ID.
     *
     * @param supplierId supplier ID to search
     * @return matching supplier, or null when not found
     */
    public Supplier searchSupplier(int supplierId) {
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierId() == supplierId) {
                return supplier;
            }
        }
        return null;
    }

    /**
     * Displays all suppliers in a formatted table.
     */
    public void displaySuppliers() {
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers available.");
            return;
        }

        System.out.println("\n--------------------------------------------------");
        System.out.printf("%-6s %-20s %-15s%n", "## ID", "Name", "Location");
        System.out.println("--------------------------------------------------");

        for (Supplier supplier : suppliers) {
            System.out.printf("%-6d %-20s %-15s%n",
                    supplier.getSupplierId(),
                    supplier.getSupplierName(),
                    supplier.getLocation());
        }
    }

    /**
     * Deletes a supplier by supplier ID.
     *
     * @param supplierId supplier ID to delete
     * @return true if the supplier was deleted, otherwise false
     */
    public boolean deleteSupplier(int supplierId) {
        Supplier supplier = searchSupplier(supplierId);
        if (supplier == null) {
            System.out.println("Supplier not found.");
            return false;
        }

        suppliers.remove(supplier);
        FileManager.saveSuppliers(suppliers);
        return true;
    }

    /**
     * Updates an existing supplier.
     *
     * @param supplierId supplier ID to update
     * @param supplierName updated supplier name
     * @param location updated location
     * @return true if the supplier was updated, otherwise false
     */
    public boolean updateSupplier(int supplierId, String supplierName, String location) {
        Supplier supplier = searchSupplier(supplierId);
        if (supplier == null) {
            System.out.println("Supplier not found.");
            return false;
        }

        supplier.setSupplierName(supplierName);
        supplier.setLocation(location);
        FileManager.saveSuppliers(suppliers);
        return true;
    }

    /**
     * Gets a copy of all suppliers.
     *
     * @return supplier list copy
     */
    public ArrayList<Supplier> getSuppliers() {
        return new ArrayList<>(suppliers);
    }
}
