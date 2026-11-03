package com.college.inventory.repository;

import com.college.inventory.entity.ProductLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ============================================================================
 * DATA ACCESS LAYER: ProductLogRepository
 * ============================================================================
 *
 * Provides persistence, retrieval, and deletion of historical audit logs:
 *   - findByProductIdOrderByTimestampDesc(): Gets log history for a specific product.
 *   - findAllByOrderByTimestampDesc(): Gets global store activity log in reverse chronological order.
 *   - deleteByProductId(): Deletes all logs for a specific product ID.
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

    /**
     * Bulk deletes all audit logs associated with a specific product ID.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM ProductLog pl WHERE pl.productId = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}
