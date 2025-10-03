package com.corporatebanking.gateway.dto.user;

public record UserCreateRequest(
        String fullName,
        String dateOfBirth,
        Long genderId,
        String email,
){}