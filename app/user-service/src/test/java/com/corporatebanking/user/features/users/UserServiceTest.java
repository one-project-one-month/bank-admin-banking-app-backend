package com.corporatebanking.user.features.users;

import com.corporatebanking.user.features.users.models.UserData;
import com.corporatebanking.user.features.users.repository.UserRepository;
import com.corporatebanking.user.features.users.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testGetUserById_UserExists() {
        // Arrange
        Long userId = 1L;
        UserData expectedUser = new UserData(
            userId, 
            "testuser", 
            "test@example.com", 
            "password123",
            null, 
            null, 
            LocalDate.now(), 
            null, 
            null, 
            null
        );
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        Optional<UserData> result = userService.getUserById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedUser.getId(), result.get().getId());
        assertEquals(expectedUser.getUsername(), result.get().getUsername());
        assertEquals(expectedUser.getEmail(), result.get().getEmail());
        verify(userRepository).findById(userId);
    }

    @Test
    void testGetUserById_UserNotFound() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<UserData> result = userService.getUserById(userId);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository).findById(userId);
    }

    @Test
    void testGetUserById_InvalidId() {
        // Test with null ID
        Optional<UserData> result1 = userService.getUserById(null);
        assertFalse(result1.isPresent());

        // Test with negative ID
        Optional<UserData> result2 = userService.getUserById(-1L);
        assertFalse(result2.isPresent());

        // Test with zero ID
        Optional<UserData> result3 = userService.getUserById(0L);
        assertFalse(result3.isPresent());

        // Verify repository was never called for invalid IDs
        verify(userRepository, never()).findById(any());
    }
}