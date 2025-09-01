package com.spectrosystems.student_management_api.service;

import com.spectrosystems.student_management_api.dto.StudentRequest;
import com.spectrosystems.student_management_api.dto.StudentResponse;
import com.spectrosystems.student_management_api.entity.Student;
import com.spectrosystems.student_management_api.exception.DuplicateEmailException;
import com.spectrosystems.student_management_api.exception.StudentNotFoundException;
import com.spectrosystems.student_management_api.mapper.StudentMapper;
import com.spectrosystems.student_management_api.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<StudentResponse> getAllStudents() {
        List<Student> students = studentRepository.findAll();

        return students.stream().map(StudentMapper::toResponse).toList();
    }

    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student with id: " + id + " not found"));
        return StudentMapper.toResponse(student);
    }


    public StudentResponse addStudent(StudentRequest studentRequest) {
        Student student = StudentMapper.toStudent(studentRequest);
        try {
            Student savedStudent = studentRepository.save(student);
            return StudentMapper.toResponse(savedStudent);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMostSpecificCause().getMessage().toLowerCase().contains("email")) {
                throw new DuplicateEmailException("Email already exists");
            }
            throw ex;
        }
    }


    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student with id: " + id + " not found"));

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setDateOfBirth(request.getDateOfBirth());

        try {
            Student updatedStudent = studentRepository.save(student);
            return StudentMapper.toResponse(updatedStudent);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMostSpecificCause().getMessage().toLowerCase().contains("email")) {
                throw new DuplicateEmailException("Email already exists");
            }
            throw ex;
        }
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student with id: " + id + " not found");
        }
        studentRepository.deleteById(id);
    }
}
