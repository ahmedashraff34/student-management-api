package com.spectrosystems.student_management_api.services;

import com.spectrosystems.student_management_api.dtos.StudentRequest;
import com.spectrosystems.student_management_api.dtos.StudentResponse;
import com.spectrosystems.student_management_api.models.Student;
import com.spectrosystems.student_management_api.exceptions.DuplicateEmailException;
import com.spectrosystems.student_management_api.exceptions.StudentNotFoundException;
import com.spectrosystems.student_management_api.mappers.StudentMapper;
import com.spectrosystems.student_management_api.repositories.StudentRepository;
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
    void retrieveAllStudents_whenCalled_shouldReturnAllStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "Ahmed", "Ashraf", "ahmed@example.com", LocalDate.of(2002, 4, 3)));
        students.add(new Student(2L, "Ahmed", "Ashraf", "ahmedddd@example.com", LocalDate.of(2002, 4, 3)));

        when(studentRepository.findAll()).thenReturn(students);

        List<StudentResponse> result = studentService.retrieveAllStudents();
        Assertions.assertEquals(students.size(), result.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void retrieveStudentById_whenStudentExists_shouldReturnStudentResponse() {
        Student student = Student.builder().id(1L).firstName("Ahmed").lastName("Ashraf").email("ahmed@example.com").dateOfBirth(LocalDate.of(2002, 4, 3)).build();

        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        StudentResponse result = studentService.retrieveStudentById(student.getId());

        Assertions.assertEquals(student.getId(), result.getId());
        Assertions.assertEquals(student.getFirstName(), result.getFirstName());
        Assertions.assertEquals(student.getLastName(), result.getLastName());
        Assertions.assertEquals(student.getEmail(), result.getEmail());
        Assertions.assertEquals(student.getDateOfBirth(), result.getDateOfBirth());

        verify(studentRepository, times(1)).findById(student.getId());
    }

    @Test
    void retrieveStudentById_whenStudentDoesNotExist_shouldThrowStudentNotFoundException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(StudentNotFoundException.class, () -> {
            studentService.retrieveStudentById(1L);
        });

        verify(studentRepository, times(1)).findById(1L);
    }


    @Test
    void createStudent_whenNewEmail_shouldSaveStudent() {
        StudentRequest studentRequest = StudentRequest.builder().firstName("Ahmed").lastName("Ashraf").email("ahmed@example.com").dateOfBirth(LocalDate.of(2002, 4, 3)).build();

        Student savedStudent = Student.builder().id(1L).firstName(studentRequest.getFirstName()).lastName(studentRequest.getLastName()).email(studentRequest.getEmail()).dateOfBirth(studentRequest.getDateOfBirth()).build();

        when(studentRepository.save(StudentMapper.toStudent(studentRequest))).thenReturn(savedStudent);

        StudentResponse result = studentService.createStudent(studentRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(savedStudent.getId(), result.getId());
        Assertions.assertEquals(savedStudent.getFirstName(), result.getFirstName());
        Assertions.assertEquals(savedStudent.getLastName(), result.getLastName());
        Assertions.assertEquals(savedStudent.getEmail(), result.getEmail());
        Assertions.assertEquals(savedStudent.getDateOfBirth(), result.getDateOfBirth());

        verify(studentRepository, times(1)).save(StudentMapper.toStudent(studentRequest));
    }

    @Test
    void createStudent_whenEmailAlreadyExists_shouldThrowDuplicateEmailException() {
        StudentRequest studentRequest = StudentRequest.builder().firstName("Ahmed").lastName("Ashraf").email("ahmed@example.com").dateOfBirth(LocalDate.of(2002, 4, 3)).build();

        when(studentRepository.save(StudentMapper.toStudent(studentRequest))).thenThrow(new DataIntegrityViolationException("Constraint violation", new Throwable("Duplicated Email")));

        Assertions.assertThrows(DuplicateEmailException.class, () -> {
            studentService.createStudent(studentRequest);
        });

        verify(studentRepository, times(1)).save(StudentMapper.toStudent(studentRequest));
    }

    @Test
    void createStudent_whenDatabaseError_shouldThrowDataIntegrityViolationException() {
        StudentRequest studentRequest = StudentRequest.builder().firstName("Ahmed").lastName("Ashraf").email("ahmed@example.com").dateOfBirth(LocalDate.of(2002, 4, 3)).build();

        when(studentRepository.save(StudentMapper.toStudent(studentRequest))).thenThrow(new DataIntegrityViolationException("Constraint violation", new Throwable("Internal DB Error")));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            studentService.createStudent(studentRequest);
        });

        verify(studentRepository, times(1)).save(StudentMapper.toStudent(studentRequest));
    }


    @Test
    void updateStudent_whenStudentExists_shouldUpdateAndReturnStudent() {
        Student oldStudent = Student.builder().id(1L).firstName("Ahmed").lastName("Ashraf").email("ahmed@example.com").dateOfBirth(LocalDate.of(2002, 4, 3)).build();

        StudentRequest updatedRequest = StudentRequest.builder().firstName("Ahmed").lastName("Ashraf").email("NEWahmed@example.com").dateOfBirth(LocalDate.of(2002, 4, 3)).build();

        Student updatedStudentEntity = Student.builder().id(1L).firstName("Ahmed").lastName("Ashraf").email("NEWahmed@example.com").dateOfBirth(LocalDate.of(2002, 4, 3)).build();

        StudentResponse expectedResponse = StudentResponse.builder().id(1L).firstName("Ahmed").lastName("Ashraf").email("NEWahmed@example.com").dateOfBirth(LocalDate.of(2002, 4, 3)).build();

        when(studentRepository.findById(oldStudent.getId())).thenReturn(Optional.of(oldStudent));
        when(studentRepository.save(oldStudent)).thenReturn(updatedStudentEntity);

        StudentResponse result = studentService.updateStudent(oldStudent.getId(), updatedRequest);

        Assertions.assertEquals(expectedResponse, result);

        verify(studentRepository, times(1)).findById(oldStudent.getId());
        verify(studentRepository, times(1)).save(oldStudent);
    }

    @Test
    void updateStudent_whenStudentDoesNotExist_shouldThrowStudentNotFoundException() {
        StudentRequest updatedRequest = StudentRequest.builder().firstName("Ahmed").lastName("Ashraf").email("NEWahmed@example.com").dateOfBirth(LocalDate.of(2002, 4, 3)).build();

        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(StudentNotFoundException.class, () -> {
            studentService.updateStudent(1L, updatedRequest);
        });

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(0)).save(any());
    }

    @Test
    void updateStudent_whenEmailAlreadyExists_shouldThrowDuplicateEmailException() {
        Student oldStudent = Student.builder().id(1L).firstName("Ahmed").lastName("Ashraf").email("ahmed@example.com").dateOfBirth(LocalDate.of(2002, 4, 3)).build();

        StudentRequest updatedRequest = StudentRequest.builder().firstName("Ahmed").lastName("Ashraf").email("NEWahmed@example.com").dateOfBirth(LocalDate.of(2002, 4, 3)).build();

        when(studentRepository.findById(oldStudent.getId())).thenReturn(Optional.of(oldStudent));
        when(studentRepository.save(oldStudent)).thenThrow(new DataIntegrityViolationException("Constraint violation", new Throwable("Duplicated Email")));

        Assertions.assertThrows(DuplicateEmailException.class, () -> {
            studentService.updateStudent(oldStudent.getId(), updatedRequest);
        });

        verify(studentRepository, times(1)).findById(oldStudent.getId());
        verify(studentRepository, times(1)).save(oldStudent);
    }

    @Test
    void updateStudent_whenDatabaseError_shouldThrowDataIntegrityViolationException() {
        Student oldStudent = Student.builder().id(1L).firstName("Ahmed").lastName("Ashraf").email("ahmed@example.com").dateOfBirth(LocalDate.of(2002, 4, 3)).build();

        StudentRequest updatedRequest = StudentRequest.builder().firstName("Ahmed").lastName("Ashraf").email("NEWahmed@example.com").dateOfBirth(LocalDate.of(2002, 4, 3)).build();

        when(studentRepository.findById(oldStudent.getId())).thenReturn(Optional.of(oldStudent));
        when(studentRepository.save(oldStudent)).thenThrow(new DataIntegrityViolationException("Constraint violation", new Throwable("Internal DB Error")));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            studentService.updateStudent(oldStudent.getId(), updatedRequest);
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
