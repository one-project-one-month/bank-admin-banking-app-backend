package com.learning.product.mapper.jdbc;

import com.learning.product.model.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProductJdbcMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getBigDecimal("item_price"));
        return product;
    }
}
