package com.spectrosystems.student_management_api.mapper;

import com.spectrosystems.student_management_api.dto.StudentRequest;
import com.spectrosystems.student_management_api.dto.StudentResponse;
import com.spectrosystems.student_management_api.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public static Student toStudent(StudentRequest request) {
        return Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .build();
    }

    public static StudentResponse toResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .dateOfBirth(student.getDateOfBirth())
                .build();
    }
}
