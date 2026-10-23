package com.college.inventory.service.impl;

import com.college.inventory.entity.Product;
import com.college.inventory.exception.ProductNotFoundException;
import com.college.inventory.repository.ProductRepository;
import com.college.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ============================================================================
 * OOP CONCEPT: POLYMORPHISM / METHOD OVERRIDING (@Override)
 * ============================================================================
 *
 * ProductServiceImpl is the concrete implementation of the ProductService interface.
 * Every method marked with @Override fulfills the abstract contract declared by the interface.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    // ==================== Validation Logic ====================

    /**
     * Enforces retail inventory business rules:
     *   1. productName, skuCode, category cannot be null or empty.
     *   2. unitPrice must be greater than 0 (> 0).
     *   3. quantity must be non-negative (>= 0).
     *   4. reorderLevel must be non-negative (>= 0).
     */
    @Override
    public void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product data cannot be empty.");
        }

        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product Name cannot be empty.");
        }

        if (product.getSkuCode() == null || product.getSkuCode().trim().isEmpty()) {
            throw new IllegalArgumentException("SKU Code cannot be empty.");
        }

        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }

        if (product.getUnitPrice() == null || product.getUnitPrice() <= 0) {
            throw new IllegalArgumentException("Unit Price must be greater than 0 (₹).");
        }

        // If packetsPerBundle is not specified yet, default gracefully to 1 (can be configured now or later)
        if (product.getPacketsPerBundle() == null || product.getPacketsPerBundle() <= 0) {
            product.setPacketsPerBundle(1);
        }

        // Calculate total stock from input bundles and loose packets if supplied
        product.calculateTotalQuantityFromInputs();

        if (product.getQuantity() == null || product.getQuantity() < 0) {
            throw new IllegalArgumentException("Total Quantity cannot be negative.");
        }

        if (product.getReorderLevel() == null || product.getReorderLevel() < 0) {
            throw new IllegalArgumentException("Reorder Level cannot be negative.");
        }
    }

    // ==================== CRUD Operations ====================

    @Override
    public Product saveProduct(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        validateProduct(productDetails);

        // Fetch existing product or throw custom ProductNotFoundException
        Product existing = getProductById(id);

        // Update inherited BaseItem fields
        existing.setProductName(productDetails.getProductName().trim());
        existing.setSkuCode(productDetails.getSkuCode().trim().toUpperCase());
        existing.setCategory(productDetails.getCategory().trim());

        // Update Product fields
        existing.setUnitPrice(productDetails.getUnitPrice());
        existing.setQuantity(productDetails.getQuantity());
        existing.setReorderLevel(productDetails.getReorderLevel());
        existing.setPacketsPerBundle(productDetails.getPacketsPerBundle());

        return productRepository.save(existing);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.searchByNameOrCategory(keyword.trim());
    }

    @Override
    public Double calculateTotalInventoryValue(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return 0.0;
        }
        return products.stream()
                .mapToDouble(Product::getTotalValue)
                .sum();
    }

    @Override
    public long countLowStockProducts(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return 0;
        }
        return products.stream()
                .filter(Product::isLowStock)
                .count();
    }
}
