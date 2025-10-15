package com.corporatebanking.faq.model;

public record Faq(
    Integer id,
    String question,
    String answer,
    Integer categoryId,
    String createdAt,
    String updatedAt
) {}
