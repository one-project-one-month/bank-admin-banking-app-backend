package com.learning.product.service.jpa;

import com.learning.product.dto.*;

import java.util.List;

public interface ProductJpaService {
    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse getProduct(Long id);
    List<ProductResponse> getAllProducts();
    ProductResponse updateProduct(Long id, UpdateProductRequest request);
    void deleteProduct(Long id);
}
