package com.corporatebanking.gateway.dto.faq;

public record UpdateFaqRequestDto(
    String question,
    String answer
) { }
