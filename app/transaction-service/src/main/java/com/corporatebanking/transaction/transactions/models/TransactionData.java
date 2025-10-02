package com.corporatebanking.transaction.transactions.models;

import java.time.LocalDate;

public record TransactionData(
        Long id,
        AccountTypeData accountType,
        String accountNumber,
        String name,
        double amount,
        String note,
        LocalDate createdAt,
        LocalDate updatedAt
) {}
