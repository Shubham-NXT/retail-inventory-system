package com.college.inventory.repository;

import com.college.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ============================================================================
 * DATA ACCESS LAYER: ProductRepository
 * ============================================================================
 *
 * Extending JpaRepository<Product, Long> provides out-of-the-box CRUD operations:
 *   - save(), findAll(), findById(), deleteById(), count()
 *
 * Custom JPQL Query:
 *   - searchByNameOrCategory(): Case-insensitive partial matching across
 *     both productName and category fields.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Search products where the keyword appears in the product name or category.
     * Case-insensitive matching powered by LOWER() and CONCAT('%', :keyword, '%').
     */
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByNameOrCategory(@Param("keyword") String keyword);
}
