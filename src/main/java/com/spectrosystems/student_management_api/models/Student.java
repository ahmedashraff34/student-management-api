package com.spectrosystems.student_management_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entity representing a student in the system.
 * Maps to the "students" table in the database.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
public class Student {

    /**
     * Unique identifier for the student.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    /**
     * First name of the student.
     */
    String firstName;

    /**
     * Last name of the student.
     */
    String lastName;

    /**
     * Email of the student.
     * Must be unique in the database.
     */
    @Column(unique = true)
    String email;

    /**
     * Date of birth of the student.
     */
    LocalDate dateOfBirth;
}
