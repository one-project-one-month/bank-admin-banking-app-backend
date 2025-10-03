package com.corporatebanking.gateway.dto.transaction;

public record CreateTransactionRequestDto(
        Long accountTypeId,
        String accountNumber,
        String name,
        double amount,
        String note
) {}
