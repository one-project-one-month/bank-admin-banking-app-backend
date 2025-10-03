package com.corporatebanking.user.features.userService.models;

import java.time.LocalDate;
import java.util.Date;

public record User(
        Long id,
        String fullName,
        Date dateOfBirth,
        int genderId,
        String email,
        LocalDate createdAt,
        LocalDate updatedAt,
        Long createdBy,
        Long updatedBy
) {}
