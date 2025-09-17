package com.learning.product.service.jdbc.impl;

import com.learning.product.dto.*;
import com.learning.product.model.Product;
import com.learning.product.repository.jdbc.ProductJdbcRepository;
import com.learning.product.service.jdbc.ProductJdbcService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductJdbcServiceImpl implements ProductJdbcService {

    private final ProductJdbcRepository jdbcRepository;

    public ProductJdbcServiceImpl(final ProductJdbcRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    @Override
    public ProductResponse createProduct(final CreateProductRequest request) {
        final Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        final Product saved = jdbcRepository.save(product);
        return new ProductResponse(saved.getId(), saved.getName(), saved.getPrice());
    }

    @Override
    public ProductResponse getProduct(final Long id) {
        return jdbcRepository.findById(id)
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getPrice()))
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return jdbcRepository.findAll().stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getPrice()))
                .toList();
    }

    @Override
    public ProductResponse updateProduct(final Long id, final UpdateProductRequest request) {
        final Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        final Product updated = jdbcRepository.update(id, product);
        return new ProductResponse(updated.getId(), updated.getName(), updated.getPrice());
    }

    @Override
    public void deleteProduct(final Long id) {
        jdbcRepository.delete(id);
    }
}
