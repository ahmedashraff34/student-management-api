package com.spectrosystems.student_management_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    long id;
    String firstName;
    String lastName;
    String email;
    LocalDate dateOfBirth;
}
