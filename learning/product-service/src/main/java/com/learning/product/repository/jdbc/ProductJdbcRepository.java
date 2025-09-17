package com.learning.product.repository.jdbc;

import com.learning.product.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductJdbcRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    Product update(Long id, Product product);
    void delete(Long id);
}
