package com.corporatebanking.transaction.model;

public record DepositTranxResponse<T> (
    Integer code,
    String message,
    T data
){}
