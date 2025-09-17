package com.learning.product.service.jpa.impl;

import com.learning.product.dto.*;
import com.learning.product.mapper.jpa.ProductJpaMapper;
import com.learning.product.model.Product;
import com.learning.product.repository.jpa.ProductJpaRepository;
import com.learning.product.service.jpa.ProductJpaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductJpaServiceImpl implements ProductJpaService {

    private final ProductJpaRepository jpaRepository;
    private final ProductJpaMapper mapper;

    public ProductJpaServiceImpl(final ProductJpaRepository jpaRepository, final ProductJpaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public ProductResponse createProduct(final CreateProductRequest request) {
        final Product product = mapper.toEntity(request);
        final Product saved = jpaRepository.save(product);
        return mapper.toResponse(saved);
    }

    @Override
    public ProductResponse getProduct(final Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return jpaRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse updateProduct(final Long id, final UpdateProductRequest request) {
        final Product product = jpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        mapper.updateEntity(request, product);
        final Product updated = jpaRepository.save(product);
        return mapper.toResponse(updated);
    }

    @Override
    public void deleteProduct(final Long id) {
        jpaRepository.deleteById(id);
    }
}
