package com.learning.product.controller.jpa;

import com.learning.product.dto.*;
import com.learning.product.service.jpa.ProductJpaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/jpa")
public class ProductJpaController {

    private final ProductJpaService service;

    public ProductJpaController(final ProductJpaService service) {
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
