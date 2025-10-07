package com.corporatebanking.user.features.accountchangestatus.model;

public record Account(
        Long id,

        String status  // "ACTIVE", "INACTIVE"

) {}
