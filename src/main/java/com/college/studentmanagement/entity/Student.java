package com.college.studentmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * ============================================================================
 * OOP CONCEPT: INHERITANCE (IS-A Relationship)
 * ============================================================================
 *
 * Student extends Person:
 *   - "A Student IS-A Person."
 *   - Student inherits 'name' and 'email' from Person.
 *   - Student defines student-specific attributes: 'id', 'course', 'totalFee', 'paidFee'.
 *
 * JPA Mapping:
 *   - @Entity: Marks this class as a database table.
 *   - @Table(name = "students"): Target MySQL table name.
 *   - Fields from both Person (name, email) and Student (id, course, totalFee, paidFee)
 *     are persisted in the "students" table.
 */
@Entity
@Table(name = "students")
public class Student extends Person {

    // ==================== Student-Specific Fields ====================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String course;      // Enrolled course (e.g., AIML, CSE, BCA)
    private Double totalFee;    // Total course fee
    private Double paidFee;     // Fee paid so far

    // ==================== Constructors ====================

    /**
     * Default no-arg constructor — required by JPA.
     */
    public Student() {
        super();
    }

    /**
     * Parameterized constructor:
     * Calls super(name, email) to initialize parent class fields,
     * demonstrating OOP Constructor Chaining.
     */
    public Student(String name, String email, String course, Double totalFee, Double paidFee) {
        super(name, email); // Initialize parent fields (Person)
        this.course = course;
        this.totalFee = totalFee;
        this.paidFee = paidFee;
    }

    // ==================== Getters and Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Double getPaidFee() {
        return paidFee;
    }

    public void setPaidFee(Double paidFee) {
        this.paidFee = paidFee;
    }

    // ==================== toString ====================

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", course='" + course + '\'' +
                ", totalFee=" + totalFee +
                ", paidFee=" + paidFee +
                '}';
    }
}
