package com.college.inventory.service.impl;

import com.college.inventory.entity.Product;
import com.college.inventory.entity.ProductLog;
import com.college.inventory.exception.ProductNotFoundException;
import com.college.inventory.repository.ProductLogRepository;
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
 * Now extended with an automated audit logging mechanism that records changes,
 * timestamps, and user remarks whenever a product is created or updated.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductLogRepository productLogRepository;

    // ==================== Validation Logic ====================

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

        // If packetsPerBundle is not specified yet, default gracefully to 1
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

    // ==================== CRUD Operations with Logging ====================

    @Override
    public Product saveProduct(Product product, String remarks) {
        validateProduct(product);
        Product saved = productRepository.save(product);

        // Record Initial Stock Creation Log
        String logRemarks = (remarks != null && !remarks.trim().isEmpty()) ? remarks.trim() : "Initial stock added to inventory";
        ProductLog log = new ProductLog(
                saved.getId(),
                saved.getProductName(),
                saved.getSkuCode(),
                "INITIAL_STOCK",
                0,
                saved.getQuantity(),
                0.0,
                saved.getUnitPrice(),
                saved.getPacketsPerBundle(),
                logRemarks
        );
        productLogRepository.save(log);

        return saved;
    }

    @Override
    public Product saveProduct(Product product) {
        return saveProduct(product, "Initial stock added to inventory");
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
    public Product updateProduct(Long id, Product productDetails, String remarks) {
        validateProduct(productDetails);

        // Fetch existing product
        Product existing = getProductById(id);

        int oldQty = (existing.getQuantity() != null) ? existing.getQuantity() : 0;
        int newQty = (productDetails.getQuantity() != null) ? productDetails.getQuantity() : 0;
        double oldPrice = (existing.getUnitPrice() != null) ? existing.getUnitPrice() : 0.0;
        double newPrice = (productDetails.getUnitPrice() != null) ? productDetails.getUnitPrice() : 0.0;

        // Determine action classification for the audit log
        String actionType = "DETAILS_UPDATE";
        if (newQty != oldQty) {
            actionType = (newQty > oldQty) ? "RESTOCK (STOCK ADDED)" : "STOCK ADJUSTMENT (REDUCED)";
        } else if (newPrice != oldPrice) {
            actionType = "PRICE_UPDATE";
        }

        // Update inherited BaseItem fields
        existing.setProductName(productDetails.getProductName().trim());
        existing.setSkuCode(productDetails.getSkuCode().trim().toUpperCase());
        existing.setCategory(productDetails.getCategory().trim());

        // Update Product fields
        existing.setUnitPrice(productDetails.getUnitPrice());
        existing.setQuantity(newQty);
        existing.setReorderLevel(productDetails.getReorderLevel());
        existing.setPacketsPerBundle(productDetails.getPacketsPerBundle());

        Product updated = productRepository.save(existing);

        // Record Update Log with Timestamp and Remarks
        String logRemarks = (remarks != null && !remarks.trim().isEmpty()) ? remarks.trim() : "Stock/details updated";
        ProductLog log = new ProductLog(
                updated.getId(),
                updated.getProductName(),
                updated.getSkuCode(),
                actionType,
                oldQty,
                newQty,
                oldPrice,
                newPrice,
                updated.getPacketsPerBundle(),
                logRemarks
        );
        productLogRepository.save(log);

        return updated;
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        return updateProduct(id, productDetails, "Stock/details updated");
    }

    @Override
    public void deleteProduct(Long id) {
        Product existing = getProductById(id);

        // Record a DELETED log before removing entity
        ProductLog log = new ProductLog(
                existing.getId(),
                existing.getProductName(),
                existing.getSkuCode(),
                "PRODUCT_DELETED",
                existing.getQuantity(),
                0,
                existing.getUnitPrice(),
                0.0,
                existing.getPacketsPerBundle(),
                "Product removed from inventory"
        );
        productLogRepository.save(log);

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

    // ==================== Audit Log Retrieval ====================

    @Override
    public List<ProductLog> getLogsForProduct(Long productId) {
        return productLogRepository.findByProductIdOrderByTimestampDesc(productId);
    }

    @Override
    public List<ProductLog> getAllLogs() {
        return productLogRepository.findAllByOrderByTimestampDesc();
    }
}
