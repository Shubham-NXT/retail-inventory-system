package com.college.inventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * ============================================================================
 * OOP CONCEPT: INHERITANCE (IS-A Relationship) & ENCAPSULATION
 * ============================================================================
 *
 * Product extends BaseItem:
 *   - "A Product IS-A BaseItem."
 *   - Product inherits 'productName', 'skuCode', and 'category' from BaseItem.
 *   - Product adds retail-specific attributes:
 *       - 'id': Primary Key
 *       - 'unitPrice': Price per individual packet/unit (₹)
 *       - 'quantity': Total individual packets in stock
 *       - 'reorderLevel': Threshold in packets for low-stock warning
 *       - 'packetsPerBundle': Ratio of packets in 1 bundle (can be set now or later; default: 1)
 *
 * Form Helpers (Transient):
 *   - 'inputBundles' & 'inputPackets': Allow stock entry in bundles, packets, or both!
 */
@Entity
@Table(name = "products")
public class Product extends BaseItem {

    // ==================== Encapsulated Fields ====================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double unitPrice;             // Price per packet/piece in ₹ (e.g. 15.00)
    private Integer quantity = 0;         // Current total packets available in stock
    private Integer reorderLevel = 0;     // Threshold in packets for 'Low Stock' alert
    private Integer packetsPerBundle = 1; // Ratio: packets in 1 bundle (default 1 = unbundled/loose)

    // ==================== Transient Form Helpers ====================
    // These allow users to add/restock inventory by specifying Bundles, Packets, or Both!
    @Transient
    private Integer inputBundles;

    @Transient
    private Integer inputPackets;

    // ==================== Constructors ====================

    /**
     * Default no-arg constructor required by JPA.
     */
    public Product() {
        super();
        this.packetsPerBundle = 1;
        this.quantity = 0;
        this.reorderLevel = 0;
    }

    /**
     * Parameterized constructor demonstrating OOP Constructor Chaining:
     * Calls super(productName, skuCode, category) to initialize parent properties.
     */
    public Product(String productName, String skuCode, String category,
                   Double unitPrice, Integer quantity, Integer reorderLevel, Integer packetsPerBundle) {
        super(productName, skuCode, category);
        this.unitPrice = unitPrice;
        this.quantity = (quantity != null) ? quantity : 0;
        this.reorderLevel = (reorderLevel != null) ? reorderLevel : 0;
        this.packetsPerBundle = (packetsPerBundle != null && packetsPerBundle > 0) ? packetsPerBundle : 1;
    }

    // ==================== Helper Methods (Business Logic in Entity) ====================

    /**
     * Computes the total inventory valuation for this product (unitPrice * quantity).
     * Avoids storing derived data in the database.
     *
     * @return total monetary value of current stock in ₹
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

    /**
     * Calculates the number of complete bundles in stock.
     * Example: 45 packets with 10 packets/bundle = 4 full bundles.
     *
     * @return integer count of full bundles
     */
    public int getBundlesCount() {
        int ppb = getPacketsPerBundle();
        if (quantity != null && ppb > 0) {
            return quantity / ppb;
        }
        return 0;
    }

    /**
     * Calculates the leftover/loose packets outside complete bundles.
     * Example: 45 packets with 10 packets/bundle = 5 loose packets.
     *
     * @return count of remaining loose packets
     */
    public int getLoosePacketsCount() {
        int ppb = getPacketsPerBundle();
        if (quantity != null && ppb > 0) {
            return quantity % ppb;
        }
        return quantity != null ? quantity : 0;
    }

    /**
     * Returns a human-friendly breakdown of stock in bundles & packets.
     * Example: "4 Bdl + 5 Pkts" or "5 Bdl (50 Pkts)"
     */
    public String getFormattedBundleStock() {
        if (quantity == null || quantity == 0) return "0 Packets";
        int ppb = getPacketsPerBundle();
        if (ppb <= 1) {
            return quantity + " Packets (Loose)";
        }
        int bundles = getBundlesCount();
        int loose = getLoosePacketsCount();
        if (loose == 0) {
            return bundles + " Bdl (" + quantity + " Pkts)";
        }
        return bundles + " Bdl + " + loose + " Pkts (" + quantity + " Total)";
    }

    /**
     * Calculates the price for 1 full bundle (unitPrice * packetsPerBundle).
     * Example: ₹15 per packet * 10 packets/bundle = ₹150 per bundle.
     */
    public Double getBundlePrice() {
        int ppb = getPacketsPerBundle();
        if (unitPrice != null) {
            return unitPrice * ppb;
        }
        return 0.0;
    }

    /**
     * Checks if this product is configured with bundle packaging (> 1).
     */
    public boolean isBundled() {
        return packetsPerBundle != null && packetsPerBundle > 1;
    }

    /**
     * Synchronizes total quantity from inputBundles and inputPackets.
     * Supports entering stock as:
     *   - Bundles only (e.g. 5 bundles = 50 packets)
     *   - Packets only (e.g. 50 packets)
     *   - Both Bundles AND Packets (e.g. 5 bundles + 3 packets = 53 packets)
     */
    public void calculateTotalQuantityFromInputs() {
        int ppb = getPacketsPerBundle();
        int bdl = (inputBundles != null && inputBundles > 0) ? inputBundles : 0;
        int pkt = (inputPackets != null && inputPackets > 0) ? inputPackets : 0;

        if (inputBundles != null || inputPackets != null) {
            this.quantity = (bdl * ppb) + pkt;
        } else if (this.quantity == null) {
            this.quantity = 0;
        }
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
        return quantity != null ? quantity : 0;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = (quantity != null) ? quantity : 0;
    }

    public Integer getReorderLevel() {
        return reorderLevel != null ? reorderLevel : 0;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = (reorderLevel != null) ? reorderLevel : 0;
    }

    public Integer getPacketsPerBundle() {
        return (packetsPerBundle != null && packetsPerBundle > 0) ? packetsPerBundle : 1;
    }

    public void setPacketsPerBundle(Integer packetsPerBundle) {
        this.packetsPerBundle = (packetsPerBundle != null && packetsPerBundle > 0) ? packetsPerBundle : 1;
    }

    public Integer getInputBundles() {
        if (inputBundles != null) {
            return inputBundles;
        }
        return getBundlesCount();
    }

    public void setInputBundles(Integer inputBundles) {
        this.inputBundles = inputBundles;
    }

    public Integer getInputPackets() {
        if (inputPackets != null) {
            return inputPackets;
        }
        return getLoosePacketsCount();
    }

    public void setInputPackets(Integer inputPackets) {
        this.inputPackets = inputPackets;
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
                ", packetsPerBundle=" + packetsPerBundle +
                '}';
    }
}
