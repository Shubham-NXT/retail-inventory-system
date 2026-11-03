package com.college.inventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ============================================================================
 * AUDIT LOG ENTITY: ProductLog
 * ============================================================================
 *
 * Captures historical changes and stock movement for every product:
 *   - Exact date & time of the change (Timestamp)
 *   - Action performed (INITIAL_STOCK, RESTOCK, PRICE_CHANGE, EDIT)
 *   - Quantity changes (Old Quantity -> New Quantity, Delta)
 *   - Unit Price changes
 *   - Custom remarks / reason for change (e.g. "Vendor batch #402 received", "Damaged goods return")
 *
 * Viva Relevance:
 *   Demonstrates enterprise audit trail principles, temporal data tracking,
 *   and entity encapsulation.
 */
@Entity
@Table(name = "product_logs")
public class ProductLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;               // ID of the target product
    private String productName;           // Preserves product name for historical record
    private String skuCode;               // Preserves SKU code

    private String actionType;            // e.g., "INITIAL_STOCK", "RESTOCK", "PRICE_UPDATE", "DETAILS_UPDATE"

    private Integer oldQuantity;          // Quantity before update
    private Integer newQuantity;          // Quantity after update
    private Integer changeInQuantity;     // Positive (+50) or negative (-10) change

    private Double oldUnitPrice;          // Price before update
    private Double newUnitPrice;          // Price after update

    private Integer packetsPerBundle;     // Bundle ratio active during this log entry

    private String remarks;               // User remarks / reason for modification

    private LocalDateTime timestamp;      // Date and time of modification

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");

    // ==================== Constructors ====================

    public ProductLog() {
        this.timestamp = LocalDateTime.now();
    }

    public ProductLog(Long productId, String productName, String skuCode, String actionType,
                      Integer oldQuantity, Integer newQuantity,
                      Double oldUnitPrice, Double newUnitPrice,
                      Integer packetsPerBundle, String remarks) {
        this.productId = productId;
        this.productName = productName;
        this.skuCode = skuCode;
        this.actionType = actionType;
        this.oldQuantity = oldQuantity;
        this.newQuantity = newQuantity;
        this.changeInQuantity = (newQuantity != null && oldQuantity != null) ? (newQuantity - oldQuantity) : 0;
        this.oldUnitPrice = oldUnitPrice;
        this.newUnitPrice = newUnitPrice;
        this.packetsPerBundle = (packetsPerBundle != null && packetsPerBundle > 0) ? packetsPerBundle : 1;
        this.remarks = (remarks != null && !remarks.trim().isEmpty()) ? remarks.trim() : "No remarks provided";
        this.timestamp = LocalDateTime.now();
    }

    // ==================== Helper Methods ====================

    /**
     * Formats timestamp into an easily readable format: '03-11-2026 02:25:30 PM'
     */
    public String getFormattedTimestamp() {
        if (timestamp != null) {
            return timestamp.format(FORMATTER);
        }
        return "N/A";
    }

    /**
     * Helper to display change with a positive sign (+) or negative sign (-)
     */
    public String getFormattedChange() {
        if (changeInQuantity == null || changeInQuantity == 0) {
            return "No change";
        }
        return (changeInQuantity > 0 ? "+" : "") + changeInQuantity + " Pkts";
    }

    // ==================== Getters & Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Integer getOldQuantity() {
        return oldQuantity;
    }

    public void setOldQuantity(Integer oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    public Integer getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(Integer newQuantity) {
        this.newQuantity = newQuantity;
    }

    public Integer getChangeInQuantity() {
        return changeInQuantity;
    }

    public void setChangeInQuantity(Integer changeInQuantity) {
        this.changeInQuantity = changeInQuantity;
    }

    public Double getOldUnitPrice() {
        return oldUnitPrice;
    }

    public void setOldUnitPrice(Double oldUnitPrice) {
        this.oldUnitPrice = oldUnitPrice;
    }

    public Double getNewUnitPrice() {
        return newUnitPrice;
    }

    public void setNewUnitPrice(Double newUnitPrice) {
        this.newUnitPrice = newUnitPrice;
    }

    public Integer getPacketsPerBundle() {
        return packetsPerBundle;
    }

    public void setPacketsPerBundle(Integer packetsPerBundle) {
        this.packetsPerBundle = packetsPerBundle;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
