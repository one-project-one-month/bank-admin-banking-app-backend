package com.sample.gateway.dto;

public record Product(
    Long id,
    String name,
    double price
) {}
