package com.corporatebanking.transaction.service;

import com.corporatebanking.transaction.dto.TransactionEvent;
import com.corporatebanking.transaction.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;

    public KafkaConsumerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "transaction-created", groupId="transaction-group")
    public void handleTransactionEvent(String message) {
        try {
            TransactionEvent event = objectMapper.readValue(message, TransactionEvent.class);
            System.out.println("Processing transaction: " + event.transactionId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
