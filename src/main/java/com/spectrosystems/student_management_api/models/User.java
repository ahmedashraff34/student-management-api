package com.spectrosystems.student_management_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entity representing a user in the system.
 * Implements Spring Security's UserDetails to integrate with authentication.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_email", columnNames = "email")
        }
)
public class User implements UserDetails {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    /**
     * First name of the user.
     */
    String firstName;

    /**
     * Last name of the user.
     */
    String lastName;

    /**
     * Username for authentication.
     * Must be unique.
     */
    String username;

    /**
     * Email of the user.
     * Must be unique.
     */
    String email;

    /**
     * Encrypted password for authentication.
     */
    String password;

    /**
     * Role assigned to the user.
     * Determines authorities/permissions.
     */
    @Enumerated(EnumType.STRING)
    Role role;

    /**
     * Returns the authorities granted to the user based on their role.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
