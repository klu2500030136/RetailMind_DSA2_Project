package retailmind.model;

/**
 * Represents a product available in the RetailMind inventory.
 */
public class Product {
    private int productId;
    private String productName;
    private String category;
    private double price;
    private int stockQuantity;
    private String barcode;

    /**
     * Creates an empty product object.
     */
    public Product() {
    }

    /**
     * Creates a product object with all product details.
     *
     * @param productId unique product ID
     * @param productName product name
     * @param category product category
     * @param price product price
     * @param stockQuantity available stock quantity
     * @param barcode product barcode
     */
    public Product(int productId, String productName, String category, double price, int stockQuantity, String barcode) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.barcode = barcode;
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
     * Gets the product name.
     *
     * @return product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the product name.
     *
     * @param productName product name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets the product category.
     *
     * @return product category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the product category.
     *
     * @param category product category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the product price.
     *
     * @return product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the product price.
     *
     * @param price product price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the stock quantity.
     *
     * @return stock quantity
     */
    public int getStockQuantity() {
        return stockQuantity;
    }

    /**
     * Sets the stock quantity.
     *
     * @param stockQuantity stock quantity
     */
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * Gets the barcode.
     *
     * @return barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * Sets the barcode.
     *
     * @param barcode barcode
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * Converts the product into comma-separated file format.
     *
     * @return product data for file storage
     */
    public String toFileString() {
        return productId + "," + productName + "," + category + "," + price + "," + stockQuantity + "," + barcode;
    }

    /**
     * Returns a readable product summary.
     *
     * @return product details
     */
    @Override
    public String toString() {
        return "Product ID: " + productId
                + ", Name: " + productName
                + ", Category: " + category
                + ", Price: " + price
                + ", Stock: " + stockQuantity
                + ", Barcode: " + barcode;
    }
}
