package com.college.studentmanagement.service.impl;

import com.college.studentmanagement.entity.Student;
import com.college.studentmanagement.exception.StudentNotFoundException;
import com.college.studentmanagement.repository.StudentRepository;
import com.college.studentmanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ============================================================================
 * OOP CONCEPT: POLYMORPHISM / METHOD OVERRIDING (@Override)
 * ============================================================================
 *
 * StudentServiceImpl provides concrete implementation for the StudentService interface.
 * Each method marked with @Override fulfills the abstract contract declared by the interface.
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // ==================== Validation Logic ====================

    /**
     * Validates student inputs before persisting or updating:
     *   1. Name, Email, and Course must not be empty.
     *   2. totalFee and paidFee must not be null or negative (< 0).
     *   3. paidFee cannot be greater than totalFee.
     */
    @Override
    public void validateStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student information cannot be null.");
        }

        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }

        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email address cannot be empty.");
        }

        if (student.getCourse() == null || student.getCourse().trim().isEmpty()) {
            throw new IllegalArgumentException("Course cannot be empty.");
        }

        if (student.getTotalFee() == null || student.getTotalFee() < 0) {
            throw new IllegalArgumentException("Total fee cannot be empty or negative.");
        }

        if (student.getPaidFee() == null || student.getPaidFee() < 0) {
            throw new IllegalArgumentException("Paid fee cannot be empty or negative.");
        }

        if (student.getPaidFee() > student.getTotalFee()) {
            throw new IllegalArgumentException("Paid fee (₹" + student.getPaidFee() + 
                    ") cannot be greater than Total fee (₹" + student.getTotalFee() + ").");
        }
    }

    // ==================== CRUD Operations ====================

    @Override
    public Student saveStudent(Student student) {
        // Enforce business validation
        validateStudent(student);
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public Student updateStudent(Long id, Student studentDetails) {
        // Enforce business validation
        validateStudent(studentDetails);

        // Fetch existing student or throw custom exception if missing
        Student existing = getStudentById(id);

        // Update fields
        existing.setName(studentDetails.getName().trim());
        existing.setEmail(studentDetails.getEmail().trim());
        existing.setCourse(studentDetails.getCourse().trim());
        existing.setTotalFee(studentDetails.getTotalFee());
        existing.setPaidFee(studentDetails.getPaidFee());

        return studentRepository.save(existing);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return studentRepository.findAll();
        }
        return studentRepository.searchByNameOrCourse(keyword.trim());
    }
}
