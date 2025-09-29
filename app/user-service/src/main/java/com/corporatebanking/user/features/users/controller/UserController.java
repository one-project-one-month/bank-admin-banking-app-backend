package com.corporatebanking.user.features.users.controller;

import com.corporatebanking.user.features.users.models.UserData;
import com.corporatebanking.user.features.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Get user by ID
     * @param id the user ID
     * @return ResponseEntity with user data or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserData> getUserById(@PathVariable Long id) {
        Optional<UserData> user = userService.getUserById(id);
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Health check endpoint
     * @return simple status message
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User service is running!");
    }
}