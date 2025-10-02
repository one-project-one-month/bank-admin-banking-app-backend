package com.corporatebanking.gateway.dto.transaction;

public record CreateTransactionResponseDto(
        boolean success,
        String message,
        Long transactionId,
        Long transactionGroupId
) {}
