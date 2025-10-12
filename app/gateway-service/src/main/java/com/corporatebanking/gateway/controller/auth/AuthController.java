package com.corporatebanking.gateway.controller;

import com.corporatebanking.gateway.dto.auth.LoginRequest;
import com.corporatebanking.gateway.dto.auth.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final RestTemplate restTemplate;

    private final String authServiceUrl = "http://auth-service:9999/auth/login";

    public AuthController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Proxying login request for user: {}", loginRequest.username());
        try {
            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                authServiceUrl,
                loginRequest,
                TokenResponse.class
            );
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException e) {
            logger.error("Error from auth-service: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Error proxying request to auth-service", e);
            return ResponseEntity.status(502).body("{\"error\": \"Bad Gateway\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}

