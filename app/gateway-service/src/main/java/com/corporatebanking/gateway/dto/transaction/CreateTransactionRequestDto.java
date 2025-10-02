package com.corporatebanking.gateway.dto.transaction;

import java.math.BigDecimal;

public record CreateTransactionRequestDto(
    Long creditAccountId,
    Long debitAccoutnId,
    BigDecimal amount,
    String transactionType,
    Long transactionGroupId
) {}
