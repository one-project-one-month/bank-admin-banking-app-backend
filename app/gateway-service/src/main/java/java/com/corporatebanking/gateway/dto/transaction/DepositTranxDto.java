package java.com.corporatebanking.gateway.dto.transaction;

import java.time.LocalDate;

public record DepositTranxDto(
        Long id,
        AccountTypeDto accountType,
        String accountNumber,
        String name,
        double amount,
        String note,
        LocalDate createdAt,
        LocalDate updatedAt
) {}
