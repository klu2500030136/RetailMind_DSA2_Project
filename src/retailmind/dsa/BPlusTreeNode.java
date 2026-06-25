package retailmind.dsa;

import java.util.ArrayList;
import java.util.List;

import retailmind.model.Product;

/**
 * Represents one linked leaf node in the academic B+ Tree implementation.
 */
public class BPlusTreeNode {
    private final List<Product> products;
    private BPlusTreeNode next;

    /**
     * Creates an empty B+ Tree leaf node.
     */
    public BPlusTreeNode() {
        products = new ArrayList<>();
    }

    /**
     * Gets products stored in this leaf.
     *
     * @return products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Gets the next linked leaf.
     *
     * @return next leaf
     */
    public BPlusTreeNode getNext() {
        return next;
    }

    /**
     * Sets the next linked leaf.
     *
     * @param next next leaf
     */
    public void setNext(BPlusTreeNode next) {
        this.next = next;
    }
}
