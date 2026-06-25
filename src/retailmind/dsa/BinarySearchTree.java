package retailmind.dsa;

import java.util.ArrayList;
import java.util.List;

import retailmind.model.Product;

/**
 * Binary Search Tree implementation for product inventory management.
 */
public class BinarySearchTree {
    private BSTNode root;

    /**
     * Inserts a product into the BST using product ID as the key.
     *
     * @param product product to insert
     * @return true if inserted, false when product is null or duplicate
     */
    public boolean insert(Product product) {
        if (product == null || isDuplicate(product.getProductId())) {
            return false;
        }

        root = insertRecursive(root, product);
        return true;
    }

    /**
     * Deletes a product from the BST by product ID.
     *
     * @param productId product ID to delete
     * @return true if deleted, false when not found
     */
    public boolean delete(int productId) {
        if (search(productId) == null) {
            return false;
        }

        root = deleteRecursive(root, productId);
        return true;
    }

    /**
     * Searches for a product by product ID.
     *
     * @param productId product ID to search
     * @return matching product, or null when not found
     */
    public Product search(int productId) {
        BSTNode current = root;

        while (current != null) {
            int currentId = current.getData().getProductId();
            if (productId == currentId) {
                return current.getData();
            }
            current = productId < currentId ? current.getLeft() : current.getRight();
        }

        return null;
    }

    /**
     * Returns products in ascending order by product ID.
     *
     * @return sorted products
     */
    public List<Product> inOrderTraversal() {
        List<Product> sortedProducts = new ArrayList<>();
        inOrderRecursive(root, sortedProducts);
        return sortedProducts;
    }

    /**
     * Checks whether a product ID already exists.
     *
     * @param productId product ID to check
     * @return true if duplicate, otherwise false
     */
    public boolean isDuplicate(int productId) {
        return search(productId) != null;
    }

    /**
     * Inserts a product recursively.
     *
     * @param node current node
     * @param product product to insert
     * @return updated node
     */
    private BSTNode insertRecursive(BSTNode node, Product product) {
        if (node == null) {
            return new BSTNode(product);
        }

        if (product.getProductId() < node.getData().getProductId()) {
            node.setLeft(insertRecursive(node.getLeft(), product));
        } else if (product.getProductId() > node.getData().getProductId()) {
            node.setRight(insertRecursive(node.getRight(), product));
        }

        return node;
    }

    /**
     * Deletes a product recursively.
     *
     * @param node current node
     * @param productId product ID to delete
     * @return updated node
     */
    private BSTNode deleteRecursive(BSTNode node, int productId) {
        if (node == null) {
            return null;
        }

        if (productId < node.getData().getProductId()) {
            node.setLeft(deleteRecursive(node.getLeft(), productId));
        } else if (productId > node.getData().getProductId()) {
            node.setRight(deleteRecursive(node.getRight(), productId));
        } else {
            if (node.getLeft() == null) {
                return node.getRight();
            }
            if (node.getRight() == null) {
                return node.getLeft();
            }

            BSTNode successor = findMinimum(node.getRight());
            node.setData(successor.getData());
            node.setRight(deleteRecursive(node.getRight(), successor.getData().getProductId()));
        }

        return node;
    }

    /**
     * Finds the node with the smallest product ID in a subtree.
     *
     * @param node subtree root
     * @return minimum node
     */
    private BSTNode findMinimum(BSTNode node) {
        BSTNode current = node;
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    /**
     * Performs recursive in-order traversal.
     *
     * @param node current node
     * @param products sorted product output
     */
    private void inOrderRecursive(BSTNode node, List<Product> products) {
        if (node == null) {
            return;
        }

        inOrderRecursive(node.getLeft(), products);
        products.add(node.getData());
        inOrderRecursive(node.getRight(), products);
    }
}
