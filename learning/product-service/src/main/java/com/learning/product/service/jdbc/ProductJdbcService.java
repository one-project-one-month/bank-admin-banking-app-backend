package com.learning.product.service.jdbc;

import com.learning.product.dto.*;

import java.util.List;

public interface ProductJdbcService {
    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse getProduct(Long id);
    List<ProductResponse> getAllProducts();
    ProductResponse updateProduct(Long id, UpdateProductRequest request);
    void deleteProduct(Long id);
}
