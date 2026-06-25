package retailmind.sorting;

import retailmind.model.Product;

/**
 * Aggregates statistics for a product including sales count, revenue, and popularity.
 */
public class ProductStats {
    private Product product;
    private int salesCount;
    private double totalRevenue;
    private double popularityScore;

    public ProductStats(Product product, int salesCount, double totalRevenue, double popularityScore) {
        this.product = product;
        this.salesCount = salesCount;
        this.totalRevenue = totalRevenue;
        this.popularityScore = popularityScore;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(double popularityScore) {
        this.popularityScore = popularityScore;
    }

    @Override
    public String toString() {
        return String.format("%-12s | %-6d | %-10.2f | %-6.2f",
                product != null ? product.getProductName() : "Unknown",
                salesCount, totalRevenue, popularityScore);
    }
}
