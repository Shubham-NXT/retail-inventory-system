package com.college.inventory.exception;

/**
 * Custom Unchecked Exception thrown when a product cannot be found
 * in the database by its primary key ID.
 *
 * Extending RuntimeException allows clean propagation to the
 * @ControllerAdvice GlobalExceptionHandler.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(Long id) {
        super("Product not found with ID: " + id);
    }
}
