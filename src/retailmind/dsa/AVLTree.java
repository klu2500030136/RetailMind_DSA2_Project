package retailmind.dsa;

import java.util.ArrayList;
import java.util.List;

import retailmind.model.Product;

/**
 * AVL Tree implementation for balanced product inventory management.
 */
public class AVLTree {
    private AVLNode root;

    /**
     * Inserts a product into the AVL tree.
     *
     * @param product product to insert
     * @return true if inserted, false when product is null or duplicate
     */
    public boolean insert(Product product) {
        if (product == null || search(product.getProductId()) != null) {
            return false;
        }

        root = insertRecursive(root, product);
        return true;
    }

    /**
     * Deletes a product by product ID.
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
        AVLNode current = root;

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
     * Returns all products in ascending order by product ID.
     *
     * @return sorted products
     */
    public List<Product> inOrderTraversal() {
        List<Product> sortedProducts = new ArrayList<>();
        inOrderRecursive(root, sortedProducts);
        return sortedProducts;
    }

    /**
     * Inserts a product recursively and balances the tree.
     *
     * @param node current node
     * @param product product to insert
     * @return updated node
     */
    private AVLNode insertRecursive(AVLNode node, Product product) {
        if (node == null) {
            return new AVLNode(product);
        }

        int productId = product.getProductId();
        int nodeProductId = node.getData().getProductId();

        if (productId < nodeProductId) {
            node.setLeft(insertRecursive(node.getLeft(), product));
        } else if (productId > nodeProductId) {
            node.setRight(insertRecursive(node.getRight(), product));
        } else {
            return node;
        }

        updateHeight(node);
        return balanceAfterInsert(node, productId);
    }

    /**
     * Deletes a product recursively and balances the tree.
     *
     * @param node current node
     * @param productId product ID to delete
     * @return updated node
     */
    private AVLNode deleteRecursive(AVLNode node, int productId) {
        if (node == null) {
            return null;
        }

        if (productId < node.getData().getProductId()) {
            node.setLeft(deleteRecursive(node.getLeft(), productId));
        } else if (productId > node.getData().getProductId()) {
            node.setRight(deleteRecursive(node.getRight(), productId));
        } else {
            if (node.getLeft() == null || node.getRight() == null) {
                AVLNode child = node.getLeft() != null ? node.getLeft() : node.getRight();
                node = child;
            } else {
                AVLNode successor = findMinimum(node.getRight());
                node.setData(successor.getData());
                node.setRight(deleteRecursive(node.getRight(), successor.getData().getProductId()));
            }
        }

        if (node == null) {
            return null;
        }

        updateHeight(node);
        return balanceAfterDelete(node);
    }

    /**
     * Performs a left rotation.
     *
     * @param node unbalanced node
     * @return new subtree root
     */
    private AVLNode leftRotate(AVLNode node) {
        AVLNode newRoot = node.getRight();
        AVLNode movedSubtree = newRoot.getLeft();

        newRoot.setLeft(node);
        node.setRight(movedSubtree);

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    /**
     * Performs a right rotation.
     *
     * @param node unbalanced node
     * @return new subtree root
     */
    private AVLNode rightRotate(AVLNode node) {
        AVLNode newRoot = node.getLeft();
        AVLNode movedSubtree = newRoot.getRight();

        newRoot.setRight(node);
        node.setLeft(movedSubtree);

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    /**
     * Gets the balance factor of a node.
     *
     * @param node node to check
     * @return balance factor
     */
    private int getBalance(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return height(node.getLeft()) - height(node.getRight());
    }

    /**
     * Gets the height of a node.
     *
     * @param node node to check
     * @return node height, or 0 for null
     */
    private int height(AVLNode node) {
        return node == null ? 0 : node.getHeight();
    }

    /**
     * Updates a node height from its children.
     *
     * @param node node to update
     */
    private void updateHeight(AVLNode node) {
        node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));
    }

    /**
     * Rebalances a subtree after insertion.
     *
     * @param node subtree root
     * @param insertedProductId inserted product ID
     * @return balanced subtree root
     */
    private AVLNode balanceAfterInsert(AVLNode node, int insertedProductId) {
        int balance = getBalance(node);

        if (balance > 1 && insertedProductId < node.getLeft().getData().getProductId()) {
            return rightRotate(node);
        }
        if (balance < -1 && insertedProductId > node.getRight().getData().getProductId()) {
            return leftRotate(node);
        }
        if (balance > 1 && insertedProductId > node.getLeft().getData().getProductId()) {
            node.setLeft(leftRotate(node.getLeft()));
            return rightRotate(node);
        }
        if (balance < -1 && insertedProductId < node.getRight().getData().getProductId()) {
            node.setRight(rightRotate(node.getRight()));
            return leftRotate(node);
        }

        return node;
    }

    /**
     * Rebalances a subtree after deletion.
     *
     * @param node subtree root
     * @return balanced subtree root
     */
    private AVLNode balanceAfterDelete(AVLNode node) {
        int balance = getBalance(node);

        if (balance > 1 && getBalance(node.getLeft()) >= 0) {
            return rightRotate(node);
        }
        if (balance > 1 && getBalance(node.getLeft()) < 0) {
            node.setLeft(leftRotate(node.getLeft()));
            return rightRotate(node);
        }
        if (balance < -1 && getBalance(node.getRight()) <= 0) {
            return leftRotate(node);
        }
        if (balance < -1 && getBalance(node.getRight()) > 0) {
            node.setRight(rightRotate(node.getRight()));
            return leftRotate(node);
        }

        return node;
    }

    /**
     * Finds the node with the smallest product ID in a subtree.
     *
     * @param node subtree root
     * @return minimum node
     */
    private AVLNode findMinimum(AVLNode node) {
        AVLNode current = node;
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
    private void inOrderRecursive(AVLNode node, List<Product> products) {
        if (node == null) {
            return;
        }

        inOrderRecursive(node.getLeft(), products);
        products.add(node.getData());
        inOrderRecursive(node.getRight(), products);
    }
}
