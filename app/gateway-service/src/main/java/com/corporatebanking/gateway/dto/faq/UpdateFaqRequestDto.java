package com.corporatebanking.gateway.dto.faq;

public record UpdateFaqRequestDto(
    int id,
    String question,
    String answer,
    int categoryId
) { }
