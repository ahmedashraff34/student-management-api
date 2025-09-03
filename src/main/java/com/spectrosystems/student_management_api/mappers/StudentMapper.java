package com.spectrosystems.student_management_api.mappers;

import com.spectrosystems.student_management_api.dtos.StudentRequest;
import com.spectrosystems.student_management_api.dtos.StudentResponse;
import com.spectrosystems.student_management_api.models.Student;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between Student entities and DTOs.
 * Provides methods to map StudentRequest to Student entity and
 * Student entity to StudentResponse DTO.
 */
@Component
public class StudentMapper {

    /**
     * Converts a StudentRequest DTO to a Student entity.
     *
     * @param request the StudentRequest containing student data
     * @return Student entity with data from the request
     */
    public static Student toStudent(StudentRequest request) {
        return Student.builder().firstName(request.getFirstName()).lastName(request.getLastName()).email(request.getEmail()).dateOfBirth(request.getDateOfBirth()).build();
    }

    /**
     * Converts a Student entity to a StudentResponse DTO.
     *
     * @param student the Student entity
     * @return StudentResponse DTO containing student data
     */
    public static StudentResponse toResponse(Student student) {
        return StudentResponse.builder().id(student.getId()).firstName(student.getFirstName()).lastName(student.getLastName()).email(student.getEmail()).dateOfBirth(student.getDateOfBirth()).build();
    }
}
