package retailmind.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retailmind.dsa.AVLTree;
import retailmind.dsa.BPlusTree;
import retailmind.dsa.BTree;
import retailmind.model.Product;
import retailmind.util.FileManager;

/**
 * Manages product inventory operations using an AVL tree and an ArrayList.
 */
public class InventoryManager {
    private final ArrayList<Product> products;
    private final AVLTree avlTree;
    private BTree bTree;
    private BPlusTree bPlusTree;

    /**
     * Creates an inventory manager, loads products from file, and rebuilds the AVL tree.
     */
    public InventoryManager() {
        FileManager.createDataFiles();
        products = FileManager.loadProducts();
        avlTree = new AVLTree();
        bTree = new BTree();
        bPlusTree = new BPlusTree();
        rebuildIndexes();
    }

    /**
     * Adds a product to the inventory.
     *
     * @param product product to add
     * @return true if the product was added, otherwise false
     */
    public boolean addProduct(Product product) {
        if (product == null || searchProduct(product.getProductId()) != null) {
            return false;
        }

        avlTree.insert(product);
        bTree.insert(product);
        bPlusTree.insert(product);
        products.add(product);
        FileManager.saveProducts(products);
        return true;
    }

    /**
     * Displays all products in the inventory.
     */
    public void displayProducts() {
        displayProductsSorted();
    }

    /**
     * Displays all products in sorted order using AVL in-order traversal.
     */
    public void displayProductsSorted() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        System.out.println("\n--------------------------------------------------------------");
        System.out.printf("%-6s %-12s %-14s %-10s %-8s %-10s%n", "## ID", "Name", "Category", "Price", "Stock",
                "Barcode");
        System.out.println("--------------------------------------------------------------");

        for (Product product : avlTree.inOrderTraversal()) {
            System.out.printf("%-6d %-12s %-14s %-10.2f %-8d %-10s%n",
                    product.getProductId(),
                    product.getProductName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    product.getBarcode());
        }
    }

    /**
     * Searches for a product by product ID using the AVL tree.
     *
     * @param id product ID to search
     * @return matching product, or null when not found
     */
    public Product searchProduct(int id) {
        return avlTree.search(id);
    }

    /**
     * Updates an existing product.
     *
     * @param id product ID to update
     * @param productName updated product name
     * @param category updated product category
     * @param price updated product price
     * @param stockQuantity updated stock quantity
     * @param barcode updated barcode
     * @return true if the product was updated, otherwise false
     */
    public boolean updateProduct(int id, String productName, String category, double price, int stockQuantity,
            String barcode) {
        Product product = searchProduct(id);
        if (product == null) {
            return false;
        }

        product.setProductName(productName);
        product.setCategory(category);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
        product.setBarcode(barcode);

        avlTree.delete(id);
        avlTree.insert(product);
        rebuildSecondaryIndexes();
        FileManager.saveProducts(products);
        return true;
    }

    /**
     * Deletes a product from the inventory.
     *
     * @param id product ID to delete
     * @return true if the product was deleted, otherwise false
     */
    public boolean deleteProduct(int id) {
        Product product = searchProduct(id);
        if (product == null) {
            return false;
        }

        avlTree.delete(id);
        products.remove(product);
        rebuildSecondaryIndexes();
        FileManager.saveProducts(products);
        return true;
    }

    /**
     * Searches for a product using the B-Tree index.
     *
     * @param id product ID to search
     * @return matching product, or null when not found
     */
    public Product searchUsingBTree(int id) {
        return bTree.search(id);
    }

    /**
     * Searches products by price range using the B+ Tree index.
     *
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return matching products
     */
    public List<Product> searchProductsByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            return new ArrayList<>();
        }

        return bPlusTree.searchRange(minPrice, maxPrice);
    }

    /**
     * Displays unique product categories.
     */
    public void displayCategories() {
        Set<String> categories = new HashSet<>();
        for (Product product : products) {
            categories.add(product.getCategory());
        }

        if (categories.isEmpty()) {
            System.out.println("No categories available.");
            return;
        }

        System.out.println("\n=========================");
        System.out.println("Available Categories");
        System.out.println("====================");
        for (String category : categories) {
            System.out.println(category);
        }
    }

    /**
     * Gets a copy of all products.
     *
     * @return product list copy
     */
    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Rebuilds the AVL tree from the ArrayList loaded from file.
     */
    private void rebuildAVLTree() {
        for (Product product : products) {
            avlTree.insert(product);
        }
    }

    /**
     * Rebuilds all product indexes from loaded products.
     */
    private void rebuildIndexes() {
        rebuildAVLTree();
        rebuildSecondaryIndexes();
    }

    /**
     * Rebuilds B-Tree and B+ Tree indexes from the current product list.
     */
    private void rebuildSecondaryIndexes() {
        bTree = new BTree();
        bPlusTree = new BPlusTree();

        for (Product product : products) {
            bTree.insert(product);
            bPlusTree.insert(product);
        }
    }
}
