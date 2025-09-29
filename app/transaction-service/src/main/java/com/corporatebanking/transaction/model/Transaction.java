package com.corporatebanking.transaction.model;

import java.math.BigDecimal;

public record Transaction (
        Long id,
        Long creditAccountId,
        Long debitAccountId,
        BigDecimal amount,
        String transactionType,
        Long transactionGroupId
){}
