package com.learning.product.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotBlank(message = "Product name cannot be empty")
        @Size(max = 100, message = "Product name must be at most 100 characters")
        String name,

        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than 0")
        @Digits(integer = 17, fraction = 2, message = "Price must be a valid monetary value with up to 2 decimals")
        BigDecimal price
) {}
