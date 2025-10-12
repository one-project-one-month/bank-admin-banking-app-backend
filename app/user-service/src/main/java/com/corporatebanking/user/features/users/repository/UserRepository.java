package com.corporatebanking.user.features.users.repository;

import com.corporatebanking.user.features.users.models.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserData, Long> {
    
    /**
     * Find user by ID
     * @param id the user ID
     * @return Optional<UserData> containing user if found, empty otherwise
     */
    Optional<UserData> findById(Long id);
    
    /**
     * Find user by username
     * @param username the username
     * @return Optional<UserData> containing user if found, empty otherwise
     */
    Optional<UserData> findByUsername(String username);
    
    /**
     * Find user by email
     * @param email the email address
     * @return Optional<UserData> containing user if found, empty otherwise
     */
    Optional<UserData> findByEmail(String email);
}