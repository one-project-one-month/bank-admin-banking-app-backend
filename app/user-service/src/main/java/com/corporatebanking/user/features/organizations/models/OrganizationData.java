package com.corporatebanking.user.features.organizations.models;

import java.time.LocalDate;

public record OrganizationData(
    Long id,
    String name,
    String shortCode,
    String address,
    String country,
    LocalDate createdAt,
    LocalDate updatedAt,
    Long createdBy,
    Long updatedBy
) {}
