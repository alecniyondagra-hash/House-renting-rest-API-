// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.repositories;

import com.housereting.houserenting.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring generates: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // Spring generates: SELECT * FROM users WHERE phone_number = ?
    Optional<User> findByPhoneNumber(String phoneNumber);

    // Spring generates: SELECT * FROM users WHERE email = ? (returns true/false)
    boolean existsByEmail(String email);
}
