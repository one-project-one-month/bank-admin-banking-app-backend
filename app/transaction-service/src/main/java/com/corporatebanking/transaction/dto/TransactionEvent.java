package com.corporatebanking.transaction.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionEvent(
        Long transactionId,
        Long groupId,
        Long creditAccountId,
        Long debitAccountId,
        BigDecimal amount,
        String transactionType,
        Instant timestamp
) {
    public static TransactionEvent of(
            Long transactionId,
            Long groupId,
            Long creditAccountId,
            Long debitAccountId,
            BigDecimal amount,
            String transactionType
    ) {
        return new TransactionEvent(
                transactionId,
                groupId,
                creditAccountId,
                debitAccountId,
                amount,
                transactionType,
                Instant.now()
        );
    }
}