package com.college.studentmanagement.service;

import com.college.studentmanagement.entity.Student;

import java.util.List;

/**
 * ============================================================================
 * OOP CONCEPT: ABSTRACTION
 * ============================================================================
 *
 * StudentService is an interface. It specifies the "CONTRACT" of what business
 * operations are available, without revealing the underlying implementation details.
 *
 * Why use an interface?
 *   - Loose coupling: Controller depends on this interface, not the concrete class.
 *   - Polymorphism: We can substitute another implementation (e.g., for mock testing)
 *     without altering any controller code.
 */
public interface StudentService {

    /**
     * Saves a new student to the database after validating inputs.
     */
    Student saveStudent(Student student);

    /**
     * Retrieves all students from the database.
     */
    List<Student> getAllStudents();

    /**
     * Finds a student by their primary key ID.
     * Throws StudentNotFoundException if the ID does not exist.
     */
    Student getStudentById(Long id);

    /**
     * Updates an existing student record with new values.
     */
    Student updateStudent(Long id, Student student);

    /**
     * Deletes a student by their ID.
     */
    void deleteStudent(Long id);

    /**
     * Searches for students whose name or course contains the given keyword.
     */
    List<Student> searchStudents(String keyword);

    /**
     * Validates student fields according to business rules.
     * Throws IllegalArgumentException if any rule is violated.
     */
    void validateStudent(Student student);
}
