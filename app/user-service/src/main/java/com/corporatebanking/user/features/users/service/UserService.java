package com.corporatebanking.user.features.users.service;

import com.corporatebanking.user.features.users.models.UserData;

import java.util.Optional;

public interface UserService {
    
    /**
     * Get user by ID
     * @param id the user ID
     * @return Optional<UserData> containing user if found, empty otherwise
     */
    Optional<UserData> getUserById(Long id);
}