package com.corporatebanking.faq.features.faq.model;

import java.time.LocalDate;

public record FaqData(
        Integer id,
        String question,
        String answer,
        LocalDate createdAt,
        LocalDate updateAt
) {}
