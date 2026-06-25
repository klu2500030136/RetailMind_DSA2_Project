package retailmind.model;

/**
 * Represents a supplier that provides products to the retail store.
 */
public class Supplier {
    private int supplierId;
    private String supplierName;
    private String location;

    /**
     * Creates an empty supplier object.
     */
    public Supplier() {
    }

    /**
     * Creates a supplier object with all supplier details.
     *
     * @param supplierId unique supplier ID
     * @param supplierName supplier name
     * @param location supplier location
     */
    public Supplier(int supplierId, String supplierName, String location) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.location = location;
    }

    /**
     * Gets the supplier ID.
     *
     * @return supplier ID
     */
    public int getSupplierId() {
        return supplierId;
    }

    /**
     * Sets the supplier ID.
     *
     * @param supplierId supplier ID
     */
    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * Gets the supplier name.
     *
     * @return supplier name
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * Sets the supplier name.
     *
     * @param supplierName supplier name
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * Gets the supplier location.
     *
     * @return supplier location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the supplier location.
     *
     * @param location supplier location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Converts the supplier into comma-separated file format.
     *
     * @return supplier data for file storage
     */
    public String toFileString() {
        return supplierId + "," + supplierName + "," + location;
    }

    /**
     * Returns a readable supplier summary.
     *
     * @return supplier details
     */
    @Override
    public String toString() {
        return "Supplier ID: " + supplierId + ", Name: " + supplierName + ", Location: " + location;
    }
}
