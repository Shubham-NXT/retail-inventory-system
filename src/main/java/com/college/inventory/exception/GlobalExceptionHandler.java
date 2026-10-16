package com.college.inventory.exception;

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
 * @ControllerAdvice intercepts exceptions thrown by any controller method.
 * Rather than exposing raw stack traces or default 500 error pages,
 * it catches exceptions and displays a polished, user-friendly error.html.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Handles product lookup failures (e.g. invalid Product ID in URL).
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(ProductNotFoundException ex, Model model) {
        model.addAttribute("errorTitle", "Product Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("timestamp", LocalDateTime.now().format(FORMATTER));
        return "error";
    }

    /**
     * Handles bad request arguments or validation failures at the service level.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorTitle", "Invalid Request");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("timestamp", LocalDateTime.now().format(FORMATTER));
        return "error";
    }

    /**
     * Catch-all handler for unexpected system or database errors.
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Application Error");
        model.addAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "An unexpected server error occurred.");
        model.addAttribute("timestamp", LocalDateTime.now().format(FORMATTER));
        return "error";
    }
}
