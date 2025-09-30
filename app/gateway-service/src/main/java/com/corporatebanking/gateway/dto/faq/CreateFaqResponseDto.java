package com.corporatebanking.gateway.dto.faq;

public record CreateFaqResponseDto(
        int id,
        String question,
        String answer,
        Long created_at,
        Long updated_at
) {
}
