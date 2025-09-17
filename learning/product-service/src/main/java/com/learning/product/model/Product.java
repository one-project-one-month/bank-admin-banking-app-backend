package com.learning.product.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    // precision = total number of digits, scale = number of digits after decimal
    @Column(name = "item_price", precision = 19, scale = 2)
    private BigDecimal price;
}
