package com.sample.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.product.dto.ProductEvent;
import com.sample.product.model.Product;
import com.sample.product.repository.ProductRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;

    public KafkaConsumerService(ObjectMapper objectMapper, ProductRepository productRepository) {
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "product-topic", groupId = "product-group")
    public void listenProductTopic(String message) {
        try {
            ProductEvent event = objectMapper.readValue(message, ProductEvent.class);
            productRepository.save(new Product(null, event.name(), event.price()));
        } catch (Exception e) {
            
        }
    }
}
