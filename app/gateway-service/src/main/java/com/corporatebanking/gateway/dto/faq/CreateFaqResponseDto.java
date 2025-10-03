package com.corporatebanking.gateway.dto.faq;

public record CreateFaqResponseDto<T>(
        int code,
        String message,
        T data
) {
}
