package com.sample.product.grpc;

import com.sample.product.grpc.ProductServiceGrpc;
import com.sample.product.model.Product;
import com.sample.product.repository.ProductRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GrpcService
public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void getProduct(GetProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        Optional<Product> product = productRepository.findById(request.getId());
        if (product.isPresent()) {
            Product p = product.get();
            ProductResponse response = ProductResponse.newBuilder()
                    .setId(p.id())
                    .setName(p.name())
                    .setPrice(p.price())
                    .build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getAllProducts(GetAllProductsRequest request, StreamObserver<ProductListResponse> responseObserver) {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponses = products.stream()
                .map(p -> ProductResponse.newBuilder()
                        .setId(p.id())
                        .setName(p.name())
                        .setPrice(p.price())
                        .build())
                .collect(Collectors.toList());

        ProductListResponse response = ProductListResponse.newBuilder()
                .addAllProducts(productResponses)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
