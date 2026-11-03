package com.college.inventory.repository;

import com.college.inventory.entity.ProductLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ============================================================================
 * DATA ACCESS LAYER: ProductLogRepository
 * ============================================================================
 *
 * Provides persistence and retrieval of historical audit logs:
 *   - findByProductIdOrderByTimestampDesc(): Gets log history for a specific product.
 *   - findAllByOrderByTimestampDesc(): Gets global store activity log in reverse chronological order.
 */
@Repository
public interface ProductLogRepository extends JpaRepository<ProductLog, Long> {

    /**
     * Retrieve all update logs for a specific product ID, newest first.
     */
    List<ProductLog> findByProductIdOrderByTimestampDesc(Long productId);

    /**
     * Retrieve all inventory logs across the system, newest first.
     */
    List<ProductLog> findAllByOrderByTimestampDesc();
}
