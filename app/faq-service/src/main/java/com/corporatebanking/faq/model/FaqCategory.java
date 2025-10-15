package com.corporatebanking.faq.model;

public record FaqCategory(
    Integer id,
    String name,
    String createdAt,
    String updatedAt
) {}
