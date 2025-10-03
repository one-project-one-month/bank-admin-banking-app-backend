package com.corporatebanking.gateway.dto.faq;

public record CreateFaqRequestDto(
        String question,
        String answer,
        int categoryId
) {}
