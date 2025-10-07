package com.corporatebanking.gateway.dto.transaction;

public record DepositTrnxRequest(
        Long accountTypeId,
        String accountNumber,
        String name,
        double amount,
        String note
) {
}
