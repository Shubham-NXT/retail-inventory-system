package com.college.studentmanagement.controller;

import com.college.studentmanagement.entity.Student;
import com.college.studentmanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * ============================================================================
 * CONTROLLER LAYER (Spring MVC)
 * ============================================================================
 *
 * Handles HTTP requests from the browser, runs input validations, delegates
 * business logic to StudentService, and returns Thymeleaf view templates.
 *
 * Key endpoints:
 *   - GET  /students              → List all students / Search by keyword
 *   - GET  /students/add          → Show form to add a new student
 *   - POST /students/save         → Save new student with validation
 *   - GET  /students/edit/{id}    → Show form to edit student / update fee
 *   - POST /students/update/{id}  → Save updated student with validation
 *   - GET  /students/delete/{id}  → Delete student by ID
 */
@Controller
public class StudentController {

    // Spring injects the StudentServiceImpl bean via interface (Abstraction)
    @Autowired
    private StudentService studentService;

    // ==================== Validation Helper ====================

    /**
     * Checks input fields against business rules:
     *   1. Name, Email, Course must not be blank.
     *   2. totalFee & paidFee must not be null or negative (< 0).
     *   3. paidFee cannot exceed totalFee.
     *
     * @return error message if invalid, or null if valid
     */
    private String validateInput(Student student) {
        if (student == null) {
            return "Student details cannot be empty.";
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            return "Student Name cannot be empty.";
        }
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            return "Email Address cannot be empty.";
        }
        if (student.getCourse() == null || student.getCourse().trim().isEmpty()) {
            return "Course cannot be empty.";
        }
        if (student.getTotalFee() == null || student.getTotalFee() < 0) {
            return "Total Fee cannot be empty or negative.";
        }
        if (student.getPaidFee() == null || student.getPaidFee() < 0) {
            return "Paid Fee cannot be empty or negative.";
        }
        if (student.getPaidFee() > student.getTotalFee()) {
            return "Paid Fee (₹" + student.getPaidFee() + ") cannot be greater than Total Fee (₹" + student.getTotalFee() + ").";
        }
        return null;
    }

    // ==================== 1. Home & List / Search ====================

    @GetMapping("/")
    public String home() {
        return "redirect:/students";
    }

    /**
     * GET /students?keyword=...
     * Lists all students, or filters them by name/course if keyword is provided.
     */
    @GetMapping("/students")
    public String listStudents(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Student> students;
        if (keyword != null && !keyword.trim().isEmpty()) {
            students = studentService.searchStudents(keyword.trim());
        } else {
            students = studentService.getAllStudents();
        }
        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword != null ? keyword.trim() : "");
        return "students-list";
    }

    // ==================== 2. Add New Student ====================

    /**
     * GET /students/add
     * Shows the registration form.
     */
    @GetMapping("/students/add")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "add-student";
    }

    /**
     * POST /students/save
     * Validates input; if valid, saves and redirects; if invalid, shows red alert on form.
     */
    @PostMapping("/students/save")
    public String saveStudent(@ModelAttribute("student") Student student, Model model) {
        String validationError = validateInput(student);
        if (validationError != null) {
            model.addAttribute("errorMessage", validationError);
            model.addAttribute("student", student);
            return "add-student";
        }

        studentService.saveStudent(student);
        return "redirect:/students";
    }

    // ==================== 3. Edit Student & Update Fee ====================

    /**
     * GET /students/edit/{id}
     * Loads the existing student details into the edit form.
     */
    @GetMapping("/students/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "edit-student";
    }

    /**
     * POST /students/update/{id}
     * Validates input; if valid, updates DB; if invalid, re-renders edit form with error alert.
     */
    @PostMapping("/students/update/{id}")
    public String updateStudent(@PathVariable("id") Long id,
                                @ModelAttribute("student") Student student,
                                Model model) {
        String validationError = validateInput(student);
        if (validationError != null) {
            student.setId(id); // ensure ID stays intact on the form
            model.addAttribute("errorMessage", validationError);
            model.addAttribute("student", student);
            return "edit-student";
        }

        studentService.updateStudent(id, student);
        return "redirect:/students";
    }

    // ==================== 4. Delete Student ====================

    /**
     * GET /students/delete/{id}
     * Deletes the student by ID and returns to the list view.
     */
    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}
