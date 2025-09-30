package com.corporatebanking.faq.features.faq.model;

import java.time.LocalDateTime;

public record CreateFaqData(
        Integer id,
        String question,
        String answer,
        LocalDateTime createdAt,
        LocalDateTime updateAt
) {}
