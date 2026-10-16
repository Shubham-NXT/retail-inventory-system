package com.college.inventory.service;

import com.college.inventory.entity.Product;

import java.util.List;

/**
 * ============================================================================
 * OOP CONCEPT: ABSTRACTION
 * ============================================================================
 *
 * ProductService is an interface representing the abstract contract of all
 * inventory business logic.
 *
 * Benefits:
 *   - Loose coupling: Controller interacts exclusively with the interface.
 *   - Extensibility: Concrete implementations can be swapped without touching controllers.
 */
public interface ProductService {

    /**
     * Persists a new product record to the database after validation.
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
     * Updates an existing product's details and restocks quantity.
     */
    Product updateProduct(Long id, Product product);

    /**
     * Deletes a product from inventory by ID.
     */
    void deleteProduct(Long id);

    /**
     * Searches products by productName or category.
     */
    List<Product> searchProducts(String keyword);

    /**
     * Enforces retail inventory business rules:
     *   - productName, skuCode, category cannot be blank
     *   - unitPrice > 0
     *   - quantity >= 0
     *   - reorderLevel >= 0
     */
    void validateProduct(Product product);

    /**
     * Computes the cumulative monetary valuation of all displayed products.
     */
    Double calculateTotalInventoryValue(List<Product> products);

    /**
     * Counts how many products are at or below their reorder threshold.
     */
    long countLowStockProducts(List<Product> products);
}
