package com.corporatebanking.gateway.dto.deposit;

import java.util.List;

public record DepositTemplateResponseDto(
    List<AccountTypeOptionDto> accountTypeOptions) {

}
