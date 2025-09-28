package com.corporatebanking.faq.features.model;

import java.time.LocalDate;

public record FaqData(
        Integer id,
        String question,
        String answer,
        LocalDate createdAt,
        LocalDate updateAt
) {}
