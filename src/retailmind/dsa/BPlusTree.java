package retailmind.dsa;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retailmind.model.Product;

/**
 * Academic B+ Tree style linked-leaf index for product price range queries.
 */
public class BPlusTree {
    private static final int LEAF_CAPACITY = 3;

    private final List<Product> productsByPrice;
    private BPlusTreeNode firstLeaf;

    /**
     * Creates an empty B+ Tree price index.
     */
    public BPlusTree() {
        productsByPrice = new ArrayList<>();
        firstLeaf = new BPlusTreeNode();
    }

    /**
     * Inserts a product and rebuilds linked leaves by ascending price.
     *
     * @param product product to insert
     */
    public void insert(Product product) {
        if (product == null) {
            return;
        }

        productsByPrice.removeIf(existing -> existing.getProductId() == product.getProductId());
        productsByPrice.add(product);
        productsByPrice.sort(Comparator.comparingDouble(Product::getPrice)
                .thenComparingInt(Product::getProductId));
        rebuildLeaves();
    }

    /**
     * Searches products whose prices are between minimum and maximum values.
     *
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return matching products
     */
    public List<Product> searchRange(double minPrice, double maxPrice) {
        List<Product> matchingProducts = new ArrayList<>();
        BPlusTreeNode current = firstLeaf;

        while (current != null) {
            for (Product product : current.getProducts()) {
                if (product.getPrice() >= minPrice && product.getPrice() <= maxPrice) {
                    matchingProducts.add(product);
                }
                if (product.getPrice() > maxPrice) {
                    return matchingProducts;
                }
            }
            current = current.getNext();
        }

        return matchingProducts;
    }

    /**
     * Rebuilds the linked leaf chain after inserts or updates.
     */
    private void rebuildLeaves() {
        if (productsByPrice.isEmpty()) {
            firstLeaf = new BPlusTreeNode();
            return;
        }

        firstLeaf = null;
        BPlusTreeNode currentLeaf = null;

        for (int index = 0; index < productsByPrice.size(); index++) {
            if (index % LEAF_CAPACITY == 0) {
                BPlusTreeNode newLeaf = new BPlusTreeNode();
                if (firstLeaf == null) {
                    firstLeaf = newLeaf;
                }
                if (currentLeaf != null) {
                    currentLeaf.setNext(newLeaf);
                }
                currentLeaf = newLeaf;
            }

            currentLeaf.getProducts().add(productsByPrice.get(index));
        }
    }
}
