package com.college.inventory.controller;

import com.college.inventory.entity.Product;
import com.college.inventory.entity.ProductLog;
import com.college.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * ============================================================================
 * CONTROLLER LAYER (Spring MVC): ProductController
 * ============================================================================
 *
 * Coordinates HTTP requests, triggers input validations, invokes ProductService,
 * records update logs with timestamps & remarks, and passes models to Thymeleaf HTML templates.
 *
 * Mappings:
 *   - GET  /                     → Redirects to /products
 *   - GET  /products             → Dashboard (List & Search products)
 *   - GET  /products/add         → Display Add Product form
 *   - POST /products/add         → Validate and save new product (with initial remarks)
 *   - GET  /products/edit/{id}   → Display Edit / Restock form
 *   - POST /products/update/{id} → Validate and update product (with update remarks)
 *   - GET  /products/delete/{id} → Delete product by ID
 *   - GET  /products/{id}/logs   → View change history & audit logs for a specific product
 *   - GET  /products/logs        → View global store inventory activity log
 */
@Controller
public class ProductController {

    // Injecting the service via interface (Abstraction)
    @Autowired
    private ProductService productService;

    // ==================== Validation Helper ====================

    /**
     * Validates input fields before saving or updating.
     * Returns error message if invalid, or null if all rules pass.
     */
    private String validateInput(Product product) {
        if (product == null) {
            return "Product data cannot be empty.";
        }
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            return "Product Name cannot be empty.";
        }
        if (product.getSkuCode() == null || product.getSkuCode().trim().isEmpty()) {
            return "SKU Code cannot be empty.";
        }
        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            return "Category cannot be empty.";
        }
        if (product.getUnitPrice() == null || product.getUnitPrice() <= 0) {
            return "Unit Price must be greater than 0 (₹).";
        }

        // Default packetsPerBundle to 1 if left empty or 0 (can be configured now or later)
        if (product.getPacketsPerBundle() == null || product.getPacketsPerBundle() <= 0) {
            product.setPacketsPerBundle(1);
        }

        // Calculate total stock from input bundles and loose packets
        product.calculateTotalQuantityFromInputs();

        if (product.getQuantity() == null || product.getQuantity() < 0) {
            return "Total Quantity cannot be negative.";
        }
        if (product.getReorderLevel() == null || product.getReorderLevel() < 0) {
            return "Reorder Level cannot be negative.";
        }
        return null;
    }

    // ==================== 1. Root & Dashboard / Search ====================

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    /**
     * GET /products
     * Displays all products or filtered products if keyword is supplied.
     * Also passes KPI summary metrics: total value & low stock count.
     */
    @GetMapping("/products")
    public String listProducts(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Product> products;
        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.searchProducts(keyword.trim());
        } else {
            products = productService.getAllProducts();
        }

        // Summary KPI Metrics
        Double totalInventoryValue = productService.calculateTotalInventoryValue(products);
        long lowStockCount = productService.countLowStockProducts(products);

        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword != null ? keyword.trim() : "");
        model.addAttribute("totalInventoryValue", totalInventoryValue);
        model.addAttribute("lowStockCount", lowStockCount);

        return "products-list";
    }

    // ==================== 2. Add New Product ====================

    /**
     * GET /products/add
     * Shows blank product entry form.
     */
    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    /**
     * POST /products/add
     * Validates input; if invalid, displays red alert; if valid, saves and records initial stock log.
     */
    @PostMapping("/products/add")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam(value = "remarks", required = false) String remarks,
                              Model model) {
        String validationError = validateInput(product);
        if (validationError != null) {
            model.addAttribute("errorMessage", validationError);
            model.addAttribute("product", product);
            model.addAttribute("remarks", remarks);
            return "add-product";
        }

        // Format SKU code to uppercase for consistency
        product.setSkuCode(product.getSkuCode().trim().toUpperCase());
        productService.saveProduct(product, remarks);
        return "redirect:/products";
    }

    // ==================== 3. Edit & Restock Product ====================

    /**
     * GET /products/edit/{id}
     * Loads product details into the edit form.
     */
    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "edit-product";
    }

    /**
     * POST /products/update/{id}
     * Validates input; if invalid, re-renders edit form with red alert; if valid, updates product and saves audit log with timestamp & remarks.
     */
    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @ModelAttribute("product") Product product,
                                @RequestParam(value = "remarks", required = false) String remarks,
                                Model model) {
        String validationError = validateInput(product);
        if (validationError != null) {
            product.setId(id); // Maintain ID on the form
            model.addAttribute("errorMessage", validationError);
            model.addAttribute("product", product);
            model.addAttribute("remarks", remarks);
            return "edit-product";
        }

        productService.updateProduct(id, product, remarks);
        return "redirect:/products";
    }

    // ==================== 4. Delete Product ====================

    /**
     * GET /products/delete/{id}
     * Removes the product, writes deletion log, and redirects back to dashboard.
     */
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    // ==================== 5. Product Update Logs & Audit Trail ====================

    /**
     * GET /products/{id}/logs
     * Displays chronological update history for a specific product.
     */
    @GetMapping("/products/{id}/logs")
    public String showProductLogs(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        List<ProductLog> logs = productService.getLogsForProduct(id);

        model.addAttribute("product", product);
        model.addAttribute("logs", logs);
        model.addAttribute("isGlobalLog", false);

        return "product-logs";
    }

    /**
     * GET /products/logs
     * Displays all stock and update logs across the entire inventory system.
     */
    @GetMapping("/products/logs")
    public String showAllLogs(Model model) {
        List<ProductLog> logs = productService.getAllLogs();

        model.addAttribute("logs", logs);
        model.addAttribute("isGlobalLog", true);

        return "product-logs";
    }

    // ==================== 6. Log Deletion Endpoints ====================

    /**
     * GET /products/logs/delete/{logId}?productId=...
     * Deletes a single audit log entry and returns to the originating logs view.
     */
    @GetMapping("/products/logs/delete/{logId}")
    public String deleteSingleLog(@PathVariable("logId") Long logId,
                                  @RequestParam(value = "productId", required = false) Long productId) {
        productService.deleteLogById(logId);
        if (productId != null) {
            return "redirect:/products/" + productId + "/logs";
        }
        return "redirect:/products/logs";
    }

    /**
     * GET /products/{id}/logs/clear
     * Purges all audit logs for a single product.
     */
    @GetMapping("/products/{id}/logs/clear")
    public String clearProductLogs(@PathVariable("id") Long id) {
        productService.deleteLogsByProductId(id);
        return "redirect:/products/" + id + "/logs";
    }

    /**
     * GET /products/logs/clear
     * Clears all audit logs across the entire store.
     */
    @GetMapping("/products/logs/clear")
    public String clearAllLogs() {
        productService.clearAllLogs();
        return "redirect:/products/logs";
    }
}
