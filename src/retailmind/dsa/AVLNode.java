package retailmind.dsa;

import retailmind.model.Product;

/**
 * Represents one node in an AVL tree of products.
 */
public class AVLNode {
    private Product data;
    private AVLNode left;
    private AVLNode right;
    private int height;

    /**
     * Creates an AVL node with product data.
     *
     * @param data product stored in the node
     */
    public AVLNode(Product data) {
        this.data = data;
        this.height = 1;
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
     * Gets the left child.
     *
     * @return left child
     */
    public AVLNode getLeft() {
        return left;
    }

    /**
     * Sets the left child.
     *
     * @param left left child
     */
    public void setLeft(AVLNode left) {
        this.left = left;
    }

    /**
     * Gets the right child.
     *
     * @return right child
     */
    public AVLNode getRight() {
        return right;
    }

    /**
     * Sets the right child.
     *
     * @param right right child
     */
    public void setRight(AVLNode right) {
        this.right = right;
    }

    /**
     * Gets the node height.
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the node height.
     *
     * @param height node height
     */
    public void setHeight(int height) {
        this.height = height;
    }
}
