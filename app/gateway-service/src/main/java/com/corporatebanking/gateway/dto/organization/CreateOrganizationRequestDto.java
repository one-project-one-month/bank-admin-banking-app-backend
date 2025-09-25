package com.corporatebanking.gateway.dto.organization;

public record CreateOrganizationRequestDto(
    String name,
    String shortcode,
    String address,
    String country,
    Long createdBy
) {}
