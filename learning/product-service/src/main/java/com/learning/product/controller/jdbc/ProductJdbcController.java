package com.learning.product.controller.jdbc;

import com.learning.product.dto.*;
import com.learning.product.service.jdbc.ProductJdbcService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/jdbc")
public class ProductJdbcController {

    private final ProductJdbcService service;

    public ProductJdbcController(final ProductJdbcService service) {
        this.service = service;
    }

    @PostMapping
    public ProductResponse create(@RequestBody @Valid final CreateProductRequest request) {
        return service.createProduct(request);
    }

    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable final Long id) {
        return service.getProduct(id);
    }

    @GetMapping
    public List<ProductResponse> getAll() {
        return service.getAllProducts();
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable final Long id,
                                  @RequestBody @Valid final UpdateProductRequest request) {
        return service.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        service.deleteProduct(id);
    }
}
