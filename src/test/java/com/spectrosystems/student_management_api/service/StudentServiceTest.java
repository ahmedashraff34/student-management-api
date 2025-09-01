package com.spectrosystems.student_management_api.service;
import com.spectrosystems.student_management_api.entity.Student;
import com.spectrosystems.student_management_api.exception.DuplicateEmailException;
import com.spectrosystems.student_management_api.exception.StudentNotFoundException;
import com.spectrosystems.student_management_api.repository.StudentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class StudentServiceTest {
    private StudentRepository studentRepository;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentRepository = Mockito.mock(StudentRepository.class);
        studentService = new StudentService(studentRepository);
    }

    @Test
    void getAllStudents_whenCalled_shouldReturnAllStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1L,"Ahmed","Ashraf","ahmed@example.com",LocalDate.of(2002,4,3)));
        students.add(new Student(2L,"Ahmed","Ashraf","ahmedddd@example.com",LocalDate.of(2002,4,3)));

        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();
        Assertions.assertEquals(students.size(), result.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getStudentById_whenStudentExists_shouldReturnStudent() {
        Student student = Student.builder()
                .id(1L)
                .firstName("Ahmed")
                .lastName("Ashraf")
                .email("ahmed@example.com")
                .dateOfBirth(LocalDate.of(2002,4,3))
                .build();

        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(student.getId());
        Assertions.assertEquals(student, result);
        verify(studentRepository, times(1)).findById(student.getId());
    }

    @Test
    void getStudentById_whenStudentDoesNotExist_shouldThrowStudentNotFoundException() {

        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentById(1L);
        });
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void addStudent_whenNewEmail_shouldSaveStudent() {
        Student student = Student.builder()
                .firstName("Ahmed")
                .lastName("Ashraf")
                .email("ahmed@example.com")
                .dateOfBirth(LocalDate.of(2002,4,3))
                .build();
        when(studentRepository.save(student)).thenReturn(student);
        Student savedStudent = studentService.addStudent(student);
        Assertions.assertNotNull(savedStudent);
        Assertions.assertEquals(student,savedStudent);
        verify(studentRepository,times(1)).save(student);
    }

    @Test
    void addStudent_whenEmailAlreadyExists_shouldThrowDuplicateEmailException() {
        Student student = Student.builder()
                .firstName("Ahmed")
                .lastName("Ashraf")
                .email("ahmed@example.com")
                .dateOfBirth(LocalDate.of(2002,4,3))
                .build();
        when(studentRepository.save(student)).thenThrow(new DataIntegrityViolationException("Constraint violation",new Throwable("Duplicated Email")));
        Assertions.assertThrows(DuplicateEmailException.class, () -> {
            studentService.addStudent(student);
        });
        verify(studentRepository,times(1)).save(student);
    }

    @Test
    void addStudent_whenDatabaseError_shouldThrowDataIntegrityViolationException() {
        Student student = Student.builder()
                .firstName("Ahmed")
                .lastName("Ashraf")
                .email("ahmed@example.com")
                .dateOfBirth(LocalDate.of(2002,4,3))
                .build();
        when(studentRepository.save(student)).thenThrow(new DataIntegrityViolationException("Constraint violation",new Throwable("Internal DB Error")));
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            studentService.addStudent(student);
        });
        verify(studentRepository,times(1)).save(student);
    }

    @Test
    void updateStudent_whenStudentExists_shouldUpdateAndReturnStudent() {
        Student oldStudent = Student.builder()
                .id(1L)
                .firstName("Ahmed")
                .lastName("Ashraf")
                .email("ahmed@example.com")
                .dateOfBirth(LocalDate.of(2002,4,3))
                .build();

        Student updatedStudent = Student.builder()
                .id(1L)
                .firstName("Ahmed")
                .lastName("Ashraf")
                .email("NEWahmed@example.com")
                .dateOfBirth(LocalDate.of(2002,4,3))
                .build();

        when(studentRepository.findById(oldStudent.getId())).thenReturn(Optional.of(oldStudent));
        when(studentRepository.save(oldStudent)).thenReturn(oldStudent);

        Student result = studentService.updateStudent(oldStudent.getId(), updatedStudent);

        Assertions.assertEquals(updatedStudent.getEmail(), result.getEmail());
        Assertions.assertEquals(updatedStudent.getFirstName(), result.getFirstName());
        Assertions.assertEquals(updatedStudent.getLastName(), result.getLastName());
        Assertions.assertEquals(updatedStudent.getDateOfBirth(), result.getDateOfBirth());

        verify(studentRepository, times(1)).findById(oldStudent.getId());
        verify(studentRepository, times(1)).save(oldStudent);
    }

    @Test
    void updateStudent_whenStudentDoesNotExist_shouldThrowStudentNotFoundException() {
        Student oldStudent = Student.builder()
                .id(1L)
                .firstName("Ahmed")
                .lastName("Ashraf")
                .email("ahmed@example.com")
                .dateOfBirth(LocalDate.of(2002,4,3))
                .build();

        Student updatedStudent = Student.builder()
                .id(1L)
                .firstName("Ahmed")
                .lastName("Ashraf")
                .email("NEWahmed@example.com")
                .dateOfBirth(LocalDate.of(2002,4,3))
                .build();

        when(studentRepository.findById(oldStudent.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(StudentNotFoundException.class, () -> {
            studentService.updateStudent(oldStudent.getId(), updatedStudent);
        });
        verify(studentRepository, times(1)).findById(oldStudent.getId());
        verify(studentRepository, times(0)).save(oldStudent);
    }

    @Test
    void updateStudent_whenEmailAlreadyExists_shouldThrowDuplicateEmailException() {
        Student oldStudent = Student.builder()
                .id(1L)
                .firstName("Ahmed")
                .lastName("Ashraf")
                .email("ahmed@example.com")
                .dateOfBirth(LocalDate.of(2002,4,3))
                .build();

        Student updatedStudent = Student.builder()
                .id(1L)
                .firstName("Ahmed")
                .lastName("Ashraf")
                .email("NEWahmed@example.com")
                .dateOfBirth(LocalDate.of(2002,4,3))
                .build();

        when(studentRepository.findById(oldStudent.getId())).thenReturn(Optional.of(oldStudent));
        when(studentRepository.save(oldStudent)).thenThrow(new DataIntegrityViolationException("Constraint violation",new Throwable("Duplicated Email")));

        Assertions.assertThrows(DuplicateEmailException.class, () -> {
            studentService.updateStudent(oldStudent.getId(), updatedStudent);
        });
        verify(studentRepository, times(1)).findById(oldStudent.getId());
        verify(studentRepository, times(1)).save(oldStudent);
    }

    @Test
    void updateStudent_whenDatabaseError_shouldThrowDataIntegrityViolationException() {
        Student oldStudent = Student.builder()
                .id(1L)
                .firstName("Ahmed")
                .lastName("Ashraf")
                .email("ahmed@example.com")
                .dateOfBirth(LocalDate.of(2002,4,3))
                .build();

        Student updatedStudent = Student.builder()
                .id(1L)
                .firstName("Ahmed")
                .lastName("Ashraf")
                .email("NEWahmed@example.com")
                .dateOfBirth(LocalDate.of(2002,4,3))
                .build();

        when(studentRepository.findById(oldStudent.getId())).thenReturn(Optional.of(oldStudent));
        when(studentRepository.save(oldStudent)).thenThrow(new DataIntegrityViolationException("Constraint violation",new Throwable("Internal DB Error")));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            studentService.updateStudent(oldStudent.getId(), updatedStudent);
        });
        verify(studentRepository, times(1)).findById(oldStudent.getId());
        verify(studentRepository, times(1)).save(oldStudent);
    }

    @Test
    void deleteStudent_whenStudentExists_shouldDeleteStudent() {
        long studentId = 1;
        when(studentRepository.existsById(studentId)).thenReturn(true);

        studentService.deleteStudent(studentId);

        verify(studentRepository, times(1)).deleteById(studentId);
    }

    @Test
    void deleteStudent_whenStudentDoesNotExist_shouldThrowStudentNotFoundException() {
        long studentId = 1;
        when(studentRepository.existsById(studentId)).thenReturn(false);

        Assertions.assertThrows(StudentNotFoundException.class, () -> {
            studentService.deleteStudent(studentId);
        });
        verify(studentRepository, times(1)).existsById(studentId);

    }


}