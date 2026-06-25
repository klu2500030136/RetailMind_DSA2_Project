package retailmind.dsa;

import retailmind.model.Product;

/**
 * Represents one node in a Binary Search Tree of products.
 */
public class BSTNode {
    private Product data;
    private BSTNode left;
    private BSTNode right;

    /**
     * Creates a BST node with product data.
     *
     * @param data product stored in the node
     */
    public BSTNode(Product data) {
        this.data = data;
    }

    /**
     * Gets the product stored in this node.
     *
     * @return product data
     */
    public Product getData() {
        return data;
    }

    /**
     * Sets the product stored in this node.
     *
     * @param data product data
     */
    public void setData(Product data) {
        this.data = data;
    }

    /**
     * Gets the left child node.
     *
     * @return left child
     */
    public BSTNode getLeft() {
        return left;
    }

    /**
     * Sets the left child node.
     *
     * @param left left child
     */
    public void setLeft(BSTNode left) {
        this.left = left;
    }

    /**
     * Gets the right child node.
     *
     * @return right child
     */
    public BSTNode getRight() {
        return right;
    }

    /**
     * Sets the right child node.
     *
     * @param right right child
     */
    public void setRight(BSTNode right) {
        this.right = right;
    }
}
