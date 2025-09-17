package com.learning.product.repository.jdbc.impl;

import com.learning.product.mapper.jdbc.ProductJdbcMapper;
import com.learning.product.model.Product;
import com.learning.product.repository.jdbc.ProductJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductJdbcRepositoryImpl implements ProductJdbcRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ProductJdbcMapper mapper;

    public ProductJdbcRepositoryImpl(final JdbcTemplate jdbcTemplate, final ProductJdbcMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public Product save(final Product product) {
        jdbcTemplate.update(
                "INSERT INTO product (name, item_price) VALUES (?, ?)",
                product.getName(), product.getPrice()
        );
        final Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        return findById(id).orElseThrow();
    }

    @Override
    public Optional<Product> findById(final Long id) {
        final List<Product> results = jdbcTemplate.query(
                "SELECT * FROM product WHERE id = ?", mapper, id
        );
        return results.stream().findFirst();
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM product", mapper);
    }

    @Override
    public Product update(final Long id, final Product product) {
        jdbcTemplate.update(
                "UPDATE product SET name = ?, item_price = ? WHERE id = ?",
                product.getName(), product.getPrice(), id
        );
        return findById(id).orElseThrow();
    }

    @Override
    public void delete(final Long id) {
        jdbcTemplate.update("DELETE FROM product WHERE id = ?", id);
    }
}
