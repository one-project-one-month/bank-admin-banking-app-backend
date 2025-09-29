package com.corporatebanking.faq.features.faq.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FaqData(
        Integer id,
        String question,
        String answer,
        LocalDateTime createdAt,
        LocalDateTime updateAt
) {}
