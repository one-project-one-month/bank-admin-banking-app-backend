package com.corporatebanking.gateway.dto.faq;

public record CreateFaqGrpcResponseDto(
        int id,
        String question,
        String answer,
        CreateFaqCategoryResponseDto createFaqCategoryResponseDto,
        Long created_at,
        Long updated_at
) { }
