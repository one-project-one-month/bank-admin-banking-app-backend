package com.corporatebanking.faq.features.faq.model;

import java.time.LocalDateTime;

public record CreateFaqCategoryData(
        Integer id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
