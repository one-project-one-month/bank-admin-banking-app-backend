package com.corporatebanking.transaction.models;

import java.time.LocalDate;

public record CreateTransactionData(
        Long id,
        CreateTransactionAccountTypeData accountType,
        String accountNumber,
        String name,
        double amount,
        String note,
        LocalDate createdAt,
        LocalDate updatedAt
) {}
