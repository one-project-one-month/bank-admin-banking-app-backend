package com.corporatebanking.gateway.dto.user;

public record UserCreateRespone(
        Long id,
        String fullName,
        String dateOfBirth,
        int genderId,
        String email,
        String createdAt,
        String updatedAt,
        Long createdBy,
        Long updatedBy
) {
}