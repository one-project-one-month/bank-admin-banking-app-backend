package com.corporatebanking.gateway.dto.transaction;

public record DepositTranxResponse<T> (
    Integer code,
    String message,
    T data
){}
