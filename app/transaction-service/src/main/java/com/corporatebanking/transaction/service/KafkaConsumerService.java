package com.corporatebanking.transaction.service;

import com.corporatebanking.transaction.dto.TransactionEvent;
import com.corporatebanking.transaction.model.Transaction;
import com.corporatebanking.transaction.repository.jdbc.TransactionJdbcRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;
    private final TransactionJdbcRepository transactionRepository;

    public KafkaConsumerService(ObjectMapper objectMapper, TransactionJdbcRepository transactionRepository) {
        this.objectMapper = objectMapper;
        this.transactionRepository = transactionRepository;
    }

    @KafkaListener(topics = "create-transaction-topic", groupId = "transaction-group")
    public void listenTransactionTopic(String message){
        try{
            TransactionEvent event = objectMapper.readValue(message, TransactionEvent.class);
            transactionRepository.save(new Transaction(
                    null,
                    event.creditAccountId(),
                    event.debitAccountId(),
                    event.amount(),
                    event.transactionType(),
                    event.groupId()
            ));
        } catch (Exception e){

        }
    }
}
