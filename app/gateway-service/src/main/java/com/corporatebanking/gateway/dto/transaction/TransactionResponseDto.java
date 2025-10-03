package com.corporatebanking.gateway.dto.transaction;

import java.time.LocalDate;

public record TransactionResponseDto(
        Long id,
        AccountTypeResponseDto accountType,
        String accountNumber,
        String name,
        double amount,
        String note,
        LocalDate createdAt,
        LocalDate updatedAt
) {}
