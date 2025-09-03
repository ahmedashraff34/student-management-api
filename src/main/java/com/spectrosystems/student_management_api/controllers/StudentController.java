package com.spectrosystems.student_management_api.controllers;

import com.spectrosystems.student_management_api.dtos.StudentRequest;
import com.spectrosystems.student_management_api.dtos.StudentResponse;
import com.spectrosystems.student_management_api.services.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing student records.
 * Provides endpoints to create, read, update, and delete students.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    /**
     * Retrieves all students.
     *
     * @return list of all students as StudentResponse DTOs wrapped in ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<StudentResponse>> retrieveAllStudents() {
        List<StudentResponse> students = studentService.retrieveAllStudents();
        return ResponseEntity.ok(students); // 200 OK
    }

    /**
     * Retrieves a single student by ID.
     *
     * @param id ID of the student to retrieve
     * @return StudentResponse DTO of the requested student wrapped in ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> retrieveStudentById(@PathVariable Long id) {
        StudentResponse student = studentService.retrieveStudentById(id);
        return ResponseEntity.ok(student); // 200 OK
    }

    /**
     * Creates a new student.
     *
     * @param request StudentRequest DTO containing student data
     * @return StudentResponse DTO of the created student wrapped in ResponseEntity
     */
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        StudentResponse createdStudent = studentService.createStudent(request);
        return ResponseEntity.status(201).body(createdStudent); // 201 Created
    }

    /**
     * Updates an existing student by ID.
     *
     * @param id      ID of the student to update
     * @param request StudentRequest DTO containing updated student data
     * @return StudentResponse DTO of the updated student wrapped in ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        StudentResponse updatedStudent = studentService.updateStudent(id, request);
        return ResponseEntity.ok(updatedStudent); // 200 OK
    }

    /**
     * Deletes a student by ID.
     *
     * @param id ID of the student to delete
     * @return ResponseEntity with HTTP 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
