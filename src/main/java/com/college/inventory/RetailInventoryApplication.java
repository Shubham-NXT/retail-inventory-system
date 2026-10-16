package com.college.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ============================================================================
 * MAIN ENTRY POINT: RETAIL INVENTORY MANAGEMENT SYSTEM
 * ============================================================================
 *
 * @SpringBootApplication encapsulates:
 *   - @Configuration: Identifies this class as a source of bean definitions.
 *   - @EnableAutoConfiguration: Instructs Spring Boot to start configuring beans
 *     based on classpath settings (Tomcat, JPA, MySQL, Thymeleaf).
 *   - @ComponentScan: Scans com.college.inventory and sub-packages for
 *     @Component, @Service, @Repository, and @Controller beans.
 */
@SpringBootApplication
public class RetailInventoryApplication {

    public static void main(String[] args) {
        // Launches embedded Tomcat server and bootstraps the Spring ApplicationContext
        SpringApplication.run(RetailInventoryApplication.class, args);
    }
}
