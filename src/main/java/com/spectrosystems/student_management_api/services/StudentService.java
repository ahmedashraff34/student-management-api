package com.spectrosystems.student_management_api.services;

import com.spectrosystems.student_management_api.dtos.StudentRequest;
import com.spectrosystems.student_management_api.dtos.StudentResponse;
import com.spectrosystems.student_management_api.models.Student;
import com.spectrosystems.student_management_api.exceptions.DuplicateEmailException;
import com.spectrosystems.student_management_api.exceptions.StudentNotFoundException;
import com.spectrosystems.student_management_api.mappers.StudentMapper;
import com.spectrosystems.student_management_api.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing student operations.
 * Handles business logic for creating, retrieving, updating, and deleting students.
 */
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    /**
     * Retrieves all students from the repository.
     *
     * @return list of all students as StudentResponse DTOs
     */
    public List<StudentResponse> retrieveAllStudents() {
        List<Student> students = studentRepository.findAll();

        // Map each Student entity to a StudentResponse DTO
        return students.stream().map(StudentMapper::toResponse).toList();
    }

    /**
     * Retrieves a student by its ID.
     *
     * @param id ID of the student to retrieve
     * @return StudentResponse DTO of the requested student
     * @throws StudentNotFoundException if no student with the given ID exists
     */
    public StudentResponse retrieveStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student with id: " + id + " not found"));
        return StudentMapper.toResponse(student);
    }

    /**
     * Creates a new student.
     *
     * @param studentRequest StudentRequest DTO containing student data
     * @return StudentResponse DTO of the created student
     * @throws DuplicateEmailException if the email already exists in the database
     */
    public StudentResponse createStudent(StudentRequest studentRequest) {
        Student student = StudentMapper.toStudent(studentRequest);
        try {
            Student savedStudent = studentRepository.save(student);
            return StudentMapper.toResponse(savedStudent);
        } catch (DataIntegrityViolationException ex) {
            // Check if the exception is caused by duplicate email
            if (ex.getMostSpecificCause().getMessage().toLowerCase().contains("email")) {
                throw new DuplicateEmailException("Email already exists");
            }
            throw ex;
        }
    }

    /**
     * Updates an existing student by ID.
     *
     * @param id      ID of the student to update
     * @param request StudentRequest DTO containing updated student data
     * @return StudentResponse DTO of the updated student
     * @throws StudentNotFoundException if no student with the given ID exists
     * @throws DuplicateEmailException  if the updated email already exists
     */
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student with id: " + id + " not found"));
        try {
            // Update fields of the existing student
            student.setFirstName(request.getFirstName());
            student.setLastName(request.getLastName());
            student.setEmail(request.getEmail());
            student.setDateOfBirth(request.getDateOfBirth());
            Student updatedStudent = studentRepository.save(student);
            return StudentMapper.toResponse(updatedStudent);
        } catch (DataIntegrityViolationException ex) {
            // Check if the exception is caused by duplicate email
            if (ex.getMostSpecificCause().getMessage().toLowerCase().contains("email")) {
                throw new DuplicateEmailException("Email already exists");
            }
            throw ex;
        }
    }

    /**
     * Deletes a student by ID.
     *
     * @param id ID of the student to delete
     * @throws StudentNotFoundException if no student with the given ID exists
     */
    public void deleteStudent(Long id) {
        // Check if the student exists before deletion
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student with id: " + id + " not found");
        }
        studentRepository.deleteById(id);
    }
}
