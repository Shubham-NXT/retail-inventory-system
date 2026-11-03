package com.college.inventory.service;

import com.college.inventory.entity.Product;
import com.college.inventory.entity.ProductLog;

import java.util.List;

/**
 * ============================================================================
 * OOP CONCEPT: ABSTRACTION
 * ============================================================================
 *
 * ProductService is an interface representing the abstract contract of all
 * inventory business logic and audit logging operations.
 */
public interface ProductService {

    /**
     * Persists a new product record and records an INITIAL_STOCK log.
     */
    Product saveProduct(Product product, String remarks);

    /**
     * Overloaded saveProduct for default remarks.
     */
    Product saveProduct(Product product);

    /**
     * Retrieves all products in the retail inventory.
     */
    List<Product> getAllProducts();

    /**
     * Retrieves a single product by its primary key ID.
     * Throws ProductNotFoundException if missing.
     */
    Product getProductById(Long id);

    /**
     * Updates an existing product's details / restocks quantity and records an audit log.
     */
    Product updateProduct(Long id, Product product, String remarks);

    /**
     * Overloaded updateProduct for backward compatibility.
     */
    Product updateProduct(Long id, Product product);

    /**
     * Deletes a product from inventory by ID and records a deletion log.
     */
    void deleteProduct(Long id);

    /**
     * Searches products by productName or category.
     */
    List<Product> searchProducts(String keyword);

    /**
     * Enforces retail inventory business rules.
     */
    void validateProduct(Product product);

    /**
     * Computes cumulative monetary valuation of all displayed products.
     */
    Double calculateTotalInventoryValue(List<Product> products);

    /**
     * Counts how many products are at or below their reorder threshold.
     */
    long countLowStockProducts(List<Product> products);

    /**
     * Fetches update and stock history logs for a specific product.
     */
    List<ProductLog> getLogsForProduct(Long productId);

    /**
     * Fetches all inventory logs across the system.
     */
    List<ProductLog> getAllLogs();
}
