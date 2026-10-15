package com.college.studentmanagement.entity;

import jakarta.persistence.MappedSuperclass;

/**
 * ============================================================================
 * OOP CONCEPT: INHERITANCE & CODE REUSABILITY
 * ============================================================================
 *
 * Person is an abstract base entity that holds common properties of any person
 * (e.g., Student, Teacher, Staff).
 *
 * @MappedSuperclass tells JPA/Hibernate:
 *   - Do NOT create a separate "person" table in MySQL.
 *   - Instead, map the fields of this class ('name' and 'email') into the table
 *     of any subclass (like 'students') that extends Person.
 *
 * OOP Pillars demonstrated:
 *   - INHERITANCE: Subclasses inherit 'name' and 'email' without repeating code.
 *   - ENCAPSULATION: Fields are private and accessed via public getters/setters.
 */
@MappedSuperclass
public abstract class Person {

    // ==================== Common Fields ====================

    private String name;   // Full name of the person
    private String email;  // Contact email address

    // ==================== Constructors ====================

    /**
     * Default no-arg constructor — required by JPA for entity reflection.
     */
    public Person() {
    }

    /**
     * Parameterized constructor — allows subclasses to initialize common fields
     * using super(name, email).
     */
    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // ==================== Getters and Setters (Encapsulation) ====================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
