package retailmind.dsa;

import java.util.ArrayList;
import java.util.List;

import retailmind.model.Product;

/**
 * B-Tree implementation for indexing products by Product ID.
 */
public class BTree {
    private static final int MINIMUM_DEGREE = 2;
    private static final int MAX_KEYS = (2 * MINIMUM_DEGREE) - 1;

    private BTreeNode root;

    /**
     * Creates an empty B-Tree.
     */
    public BTree() {
        root = new BTreeNode(true);
    }

    /**
     * Inserts a product into the B-Tree.
     *
     * @param product product to insert
     * @return true when inserted, false for null or duplicate products
     */
    public boolean insert(Product product) {
        if (product == null || search(product.getProductId()) != null) {
            return false;
        }

        if (root.getProducts().size() == MAX_KEYS) {
            BTreeNode newRoot = new BTreeNode(false);
            newRoot.getChildren().add(root);
            splitChild(newRoot, 0);
            root = newRoot;
        }

        insertNonFull(root, product);
        return true;
    }

    /**
     * Searches for a product by Product ID.
     *
     * @param productId product ID
     * @return matching product, or null when not found
     */
    public Product search(int productId) {
        return search(root, productId);
    }

    /**
     * Traverses products in sorted Product ID order.
     *
     * @return sorted products
     */
    public List<Product> traverse() {
        List<Product> sortedProducts = new ArrayList<>();
        traverse(root, sortedProducts);
        return sortedProducts;
    }

    /**
     * Inserts a product into a node that is not full.
     *
     * @param node target node
     * @param product product to insert
     */
    private void insertNonFull(BTreeNode node, Product product) {
        int index = node.getProducts().size() - 1;

        if (node.isLeaf()) {
            node.getProducts().add(null);
            while (index >= 0 && product.getProductId() < node.getProducts().get(index).getProductId()) {
                node.getProducts().set(index + 1, node.getProducts().get(index));
                index--;
            }
            node.getProducts().set(index + 1, product);
            return;
        }

        while (index >= 0 && product.getProductId() < node.getProducts().get(index).getProductId()) {
            index--;
        }
        index++;

        if (node.getChildren().get(index).getProducts().size() == MAX_KEYS) {
            splitChild(node, index);
            if (product.getProductId() > node.getProducts().get(index).getProductId()) {
                index++;
            }
        }

        insertNonFull(node.getChildren().get(index), product);
    }

    /**
     * Splits a full child node.
     *
     * @param parent parent node
     * @param childIndex index of the full child
     */
    private void splitChild(BTreeNode parent, int childIndex) {
        BTreeNode fullChild = parent.getChildren().get(childIndex);
        BTreeNode newChild = new BTreeNode(fullChild.isLeaf());

        Product middleProduct = fullChild.getProducts().get(MINIMUM_DEGREE - 1);

        for (int index = MINIMUM_DEGREE; index < fullChild.getProducts().size(); index++) {
            newChild.getProducts().add(fullChild.getProducts().get(index));
        }

        while (fullChild.getProducts().size() > MINIMUM_DEGREE - 1) {
            fullChild.getProducts().remove(fullChild.getProducts().size() - 1);
        }

        if (!fullChild.isLeaf()) {
            for (int index = MINIMUM_DEGREE; index < fullChild.getChildren().size(); index++) {
                newChild.getChildren().add(fullChild.getChildren().get(index));
            }
            while (fullChild.getChildren().size() > MINIMUM_DEGREE) {
                fullChild.getChildren().remove(fullChild.getChildren().size() - 1);
            }
        }

        parent.getChildren().add(childIndex + 1, newChild);
        parent.getProducts().add(childIndex, middleProduct);
    }

    /**
     * Searches recursively from a node.
     *
     * @param node current node
     * @param productId product ID
     * @return matching product, or null
     */
    private Product search(BTreeNode node, int productId) {
        int index = 0;
        while (index < node.getProducts().size()
                && productId > node.getProducts().get(index).getProductId()) {
            index++;
        }

        if (index < node.getProducts().size() && productId == node.getProducts().get(index).getProductId()) {
            return node.getProducts().get(index);
        }

        if (node.isLeaf()) {
            return null;
        }

        return search(node.getChildren().get(index), productId);
    }

    /**
     * Performs recursive sorted traversal.
     *
     * @param node current node
     * @param sortedProducts output list
     */
    private void traverse(BTreeNode node, List<Product> sortedProducts) {
        int index;
        for (index = 0; index < node.getProducts().size(); index++) {
            if (!node.isLeaf()) {
                traverse(node.getChildren().get(index), sortedProducts);
            }
            sortedProducts.add(node.getProducts().get(index));
        }

        if (!node.isLeaf()) {
            traverse(node.getChildren().get(index), sortedProducts);
        }
    }
}
