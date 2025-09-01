package com.spectrosystems.student_management_api.service;

import com.spectrosystems.student_management_api.entity.Student;
import com.spectrosystems.student_management_api.exception.DuplicateEmailException;
import com.spectrosystems.student_management_api.exception.StudentNotFoundException;
import com.spectrosystems.student_management_api.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student with id: " + id + " not found"));
    }

    public Student addStudent(Student student) {
        try {
            return studentRepository.save(student);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMostSpecificCause().getMessage().toLowerCase().contains("email")) {
                throw new DuplicateEmailException("Email already exists");
            }
            throw ex;
        }
    }

    public Student updateStudent(Long id, Student request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student with id: " + id + " not found"));

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setDateOfBirth(request.getDateOfBirth());

        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student with id: " + id + " not found");
        }
        studentRepository.deleteById(id);
    }
}
