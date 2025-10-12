package com.corporatebanking.gateway.dto.transaction;

import java.time.LocalDate;
public record DeleteTransactionsDTO(
        Long id,
        DeleteAccountTypeDTO accountType,
        String accountNumber,
        String name,
        Double amount,
        String note,
        LocalDate createdAt,
        LocalDate updatedAt
) {}