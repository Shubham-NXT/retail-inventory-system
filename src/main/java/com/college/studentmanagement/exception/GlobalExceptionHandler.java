package com.college.studentmanagement.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ============================================================================
 * EXCEPTION HANDLING: GLOBAL CONTROLLER ADVICE
 * ============================================================================
 *
 * @ControllerAdvice intercepts exceptions thrown across ALL controllers
 * in the application. Instead of showing ugly stack traces to the user,
 * it catches the exception and renders a user-friendly error.html page.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Handles StudentNotFoundException (e.g., student ID doesn't exist).
     */
    @ExceptionHandler(StudentNotFoundException.class)
    public String handleStudentNotFound(StudentNotFoundException ex, Model model) {
        model.addAttribute("errorTitle", "Student Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("timestamp", LocalDateTime.now().format(FORMATTER));
        return "error";
    }

    /**
     * Handles bad inputs or illegal arguments.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorTitle", "Invalid Request");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("timestamp", LocalDateTime.now().format(FORMATTER));
        return "error";
    }

    /**
     * Fallback handler for all unexpected or database errors.
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Unexpected Application Error");
        model.addAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "An unexpected server error occurred.");
        model.addAttribute("timestamp", LocalDateTime.now().format(FORMATTER));
        return "error";
    }
}
