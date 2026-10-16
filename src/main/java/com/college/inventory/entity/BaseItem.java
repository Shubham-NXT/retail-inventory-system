package com.college.inventory.entity;

import jakarta.persistence.MappedSuperclass;

/**
 * ============================================================================
 * OOP CONCEPT: INHERITANCE & CODE REUSABILITY
 * ============================================================================
 *
 * BaseItem is an abstract base class holding core retail item properties.
 *
 * @MappedSuperclass tells JPA / Hibernate:
 *   - Do NOT generate a standalone "base_item" table in MySQL.
 *   - Instead, map these common columns ('product_name', 'sku_code', 'category')
 *     directly into child tables (such as 'products') that inherit from BaseItem.
 *
 * OOP Pillars demonstrated:
 *   - INHERITANCE: Subclasses inherit common item attributes without code duplication.
 *   - ENCAPSULATION: Fields are private and accessed solely via public getters/setters.
 */
@MappedSuperclass
public abstract class BaseItem {

    // ==================== Common Fields ====================

    private String productName;  // Name of the retail product (e.g. "Wireless Mouse")
    private String skuCode;      // Unique Stock Keeping Unit (e.g. "SKU-WM-001")
    private String category;     // Product category (e.g. "Electronics", "Groceries")

    // ==================== Constructors ====================

    /**
     * Default no-arg constructor — required by JPA/Hibernate for reflection.
     */
    public BaseItem() {
    }

    /**
     * Parameterized constructor for subclasses to chain initialization via super(...).
     */
    public BaseItem(String productName, String skuCode, String category) {
        this.productName = productName;
        this.skuCode = skuCode;
        this.category = category;
    }

    // ==================== Getters & Setters (Encapsulation) ====================

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
