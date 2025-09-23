package com.sample.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.gateway.dto.Product;
import com.sample.product.grpc.GetAllProductsRequest;
import com.sample.product.grpc.GetProductRequest;
import com.sample.product.grpc.ProductListResponse;
import com.sample.product.grpc.ProductResponse;
import com.sample.product.grpc.ProductServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @GrpcClient("product-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    public ProductController(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/product")
    public void createProduct(@RequestBody Product product) throws Exception {
        String message = objectMapper.writeValueAsString(product);
        kafkaTemplate.send("product-topic", message);
    }

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable Long id) {
        GetProductRequest request = GetProductRequest.newBuilder().setId(id).build();
        ProductResponse response = productServiceBlockingStub.getProduct(request);
        return new Product(response.getId(), response.getName(), response.getPrice());
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        GetAllProductsRequest request = GetAllProductsRequest.newBuilder().build();
        ProductListResponse response = productServiceBlockingStub.getAllProducts(request);
        return response.getProductsList().stream()
                .map(p -> new Product(p.getId(), p.getName(), p.getPrice()))
                .collect(Collectors.toList());
    }
}
