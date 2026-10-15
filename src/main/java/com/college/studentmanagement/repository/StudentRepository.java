package com.college.studentmanagement.repository;

import com.college.studentmanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * StudentRepository — Data Access Layer.
 *
 * Extending JpaRepository provides ready-made CRUD methods:
 *   - save(), findAll(), findById(), deleteById()
 *
 * Custom JPQL Query:
 *   - searchByNameOrCourse(): Performs a case-insensitive search matching
 *     either the student's name OR their enrolled course.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Search students by keyword in name or course (case-insensitive).
     *
     * JPQL uses the entity property names (s.name inherited from Person, and s.course).
     */
    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.course) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Student> searchByNameOrCourse(@Param("keyword") String keyword);
}
