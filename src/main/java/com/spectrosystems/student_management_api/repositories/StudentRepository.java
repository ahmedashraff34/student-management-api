package com.spectrosystems.student_management_api.repositories;

import com.spectrosystems.student_management_api.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on Student entities.
 * Extends JpaRepository to provide standard database operations.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
