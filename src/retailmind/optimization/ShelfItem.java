package retailmind.optimization;

import retailmind.model.Product;

/**
 * Represents a product's footprint and profit for knapsack problems.
 */
public class ShelfItem {
    private Product product;
    private double shelfSpace;
    private double profit;

    public ShelfItem(Product product, double shelfSpace, double profit) {
        this.product = product;
        this.shelfSpace = shelfSpace;
        this.profit = profit;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getShelfSpace() {
        return shelfSpace;
    }

    public void setShelfSpace(double shelfSpace) {
        this.shelfSpace = shelfSpace;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    /**
     * Used for fractional knapsack (Greedy sorting).
     * @return profit per unit of space
     */
    public double getProfitPerSpace() {
        if (shelfSpace <= 0) return 0;
        return profit / shelfSpace;
    }

    @Override
    public String toString() {
        return product != null ? product.getProductName() : "Unknown Item";
    }
}
