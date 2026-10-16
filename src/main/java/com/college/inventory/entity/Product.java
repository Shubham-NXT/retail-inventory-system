package com.college.inventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * ============================================================================
 * OOP CONCEPT: INHERITANCE (IS-A Relationship) & ENCAPSULATION
 * ============================================================================
 *
 * Product extends BaseItem:
 *   - "A Product IS-A BaseItem."
 *   - Product inherits 'productName', 'skuCode', and 'category' from BaseItem.
 *   - Product adds retail-specific attributes: 'id', 'unitPrice', 'quantity', 'reorderLevel'.
 *
 * Table Mapping:
 *   - @Entity: Declares this class as a persistent entity.
 *   - @Table(name = "products"): Generates the 'products' table in MySQL.
 */
@Entity
@Table(name = "products")
public class Product extends BaseItem {

    // ==================== Encapsulated Fields ====================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double unitPrice;       // Price per unit in ₹ (e.g. 499.50)
    private Integer quantity;       // Current stock available in warehouse
    private Integer reorderLevel;   // Threshold below which product triggers 'Low Stock' alert

    // ==================== Constructors ====================

    /**
     * Default no-arg constructor required by JPA.
     */
    public Product() {
        super();
    }

    /**
     * Parameterized constructor demonstrating OOP Constructor Chaining:
     * Calls super(productName, skuCode, category) to initialize parent properties.
     */
    public Product(String productName, String skuCode, String category,
                   Double unitPrice, Integer quantity, Integer reorderLevel) {
        super(productName, skuCode, category);
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
    }

    // ==================== Helper Methods (Business Logic in Entity) ====================

    /**
     * Computes the total inventory valuation for this product (unitPrice * quantity).
     * Avoids storing derived data in the database.
     *
     * @return total monetary value of current stock
     */
    public Double getTotalValue() {
        if (unitPrice != null && quantity != null) {
            return unitPrice * quantity;
        }
        return 0.0;
    }

    /**
     * Determines whether stock has fallen to or below the reorder threshold.
     * Used by the view layer to render a Red 'Low Stock' alert badge.
     *
     * @return true if quantity <= reorderLevel, false otherwise
     */
    public boolean isLowStock() {
        if (quantity != null && reorderLevel != null) {
            return quantity <= reorderLevel;
        }
        return false;
    }

    // ==================== Getters & Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    // ==================== toString ====================

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + getProductName() + '\'' +
                ", skuCode='" + getSkuCode() + '\'' +
                ", category='" + getCategory() + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", reorderLevel=" + reorderLevel +
                '}';
    }
}
