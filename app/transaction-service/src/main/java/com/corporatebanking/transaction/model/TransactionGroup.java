package com.corporatebanking.transaction.model;

import java.math.BigDecimal;

public record TransactionGroup(
        Long id,
        Long creditorOrg,
        BigDecimal totalAmount,
        String transactionType,
        String fileUrl,
        String fileStatus,
        Long ruleSetId,
        Long groupId,
        String fileName,
        Long makerId
) {}
