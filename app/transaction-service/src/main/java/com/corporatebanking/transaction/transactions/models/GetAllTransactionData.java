package com.corporatebanking.transaction.transactions.models;

import java.time.LocalDate;


public record GetAllTransactionData(
    Long id,
    GetAllAccountTypeData accountType,
    String accountNumber,
    String name,
    double amount,
    String note,
    LocalDate createdAt,
    LocalDate updatedAt
) {}
