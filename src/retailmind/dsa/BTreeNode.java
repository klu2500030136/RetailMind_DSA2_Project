package retailmind.dsa;

import java.util.ArrayList;
import java.util.List;

import retailmind.model.Product;

/**
 * Represents one node in a B-Tree product index.
 */
public class BTreeNode {
    private final List<Product> products;
    private final List<BTreeNode> children;
    private boolean leaf;

    /**
     * Creates a B-Tree node.
     *
     * @param leaf true when this node is a leaf
     */
    public BTreeNode(boolean leaf) {
        this.products = new ArrayList<>();
        this.children = new ArrayList<>();
        this.leaf = leaf;
    }

    /**
     * Gets products stored in this node.
     *
     * @return product keys
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Gets child pointers for this node.
     *
     * @return child nodes
     */
    public List<BTreeNode> getChildren() {
        return children;
    }

    /**
     * Checks whether this node is a leaf.
     *
     * @return true if leaf, otherwise false
     */
    public boolean isLeaf() {
        return leaf;
    }

    /**
     * Updates leaf status.
     *
     * @param leaf true when this node is a leaf
     */
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
