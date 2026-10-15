package com.college.studentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point of the Student Management System.
 *
 * @SpringBootApplication combines three annotations:
 *   - @Configuration    → marks this class as a source of bean definitions
 *   - @EnableAutoConfiguration → tells Spring Boot to auto-configure beans
 *   - @ComponentScan    → scans this package (and sub-packages) for components
 */
@SpringBootApplication
public class StudentManagementApplication {

    public static void main(String[] args) {
        // Launches the embedded Tomcat server and initialises the Spring context
        SpringApplication.run(StudentManagementApplication.class, args);
    }
}
