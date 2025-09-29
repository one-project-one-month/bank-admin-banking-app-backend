package com.corporatebanking.transaction.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionEvent(
        Long transactionId,
        Long creditAccountId,
        Long debitAccountId,
        BigDecimal amount,
        String transactionType,
        Long transactionGroupId,
        Instant timestamp
) {
    public static TransactionEvent of(
            Long transactionId,
            Long creditAccountId,
            Long debitAccountId,
            BigDecimal amount,
            String transactionType,
            Long transactionGroupId
    ){
        return new TransactionEvent(
                transactionId,
                creditAccountId,
                debitAccountId,
                amount,
                transactionType,
                transactionGroupId,
                Instant.now()
        );
    }
}
