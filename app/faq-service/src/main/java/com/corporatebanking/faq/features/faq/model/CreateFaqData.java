package com.corporatebanking.faq.features.faq.model;

import java.time.LocalDateTime;

public record CreateFaqData(
        Integer id,
        String question,
        String answer,
        Integer categoryId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
