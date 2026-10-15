package com.college.studentmanagement.exception;

/**
 * Custom Exception thrown whenever a requested Student ID
 * cannot be found in the database.
 *
 * Extending RuntimeException allows it to be an unchecked exception,
 * which integrates cleanly with Spring's transaction management and
 * @ControllerAdvice exception handlers.
 */
public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String message) {
        super(message);
    }

    public StudentNotFoundException(Long id) {
        super("Student not found with ID: " + id);
    }
}
