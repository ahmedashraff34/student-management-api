package com.spectrosystems.student_management_api.repositories;

import com.spectrosystems.student_management_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on User entities.
 * Extends JpaRepository to provide standard database operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return an Optional containing the User if found, or empty otherwise
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their email address.
     *
     * @param email the email to search for
     * @return an Optional containing the User if found, or empty otherwise
     */
    Optional<User> findByEmail(String email);
}
