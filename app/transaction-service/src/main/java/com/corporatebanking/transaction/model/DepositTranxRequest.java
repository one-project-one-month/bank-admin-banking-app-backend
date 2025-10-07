package com.corporatebanking.transaction.model;

public record DepositTranxRequest(
        Long accountTypeId,
        String accountNumber,
        String name,
        double amount,
        String note
) {
}
