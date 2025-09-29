package com.corporatebanking.transaction.service;

import com.corporatebanking.transaction.dto.TransactionEvent;
import com.corporatebanking.transaction.model.Transaction;
import com.corporatebanking.transaction.model.TransactionGroup;
import com.corporatebanking.transaction.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public TransactionService(TransactionRepository transactionRepository, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.transactionRepository = transactionRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public TransactionCreated createTransaction (
            Long creditAccountId,
            Long debitAccountId,
            BigDecimal amount,
            String transactionType,
            Long transactionGroupId
    ) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        TransactionGroup group;
        if (transactionGroupId == null && transactionGroupId > 0 ) {
            group = transactionRepository.findTransactionGroupById(transactionGroupId);
            if (group == null) {
                throw new IllegalArgumentException("TransactionGroup not found");
            }
        } else {
            group = new TransactionGroup(
                    null,
                    1L,
                    amount,
                    transactionType,
                    null,
                    "PENDING",
                    null,
                    null,
                    "auto-generated",
                    1L
            );
            group = transactionRepository.saveTransactionGroup(group);
        }

        Transaction tx = new Transaction(
                null,
                creditAccountId,
                debitAccountId,
                amount,
                transactionType,
                group.id()
        );
        tx = transactionRepository.createTransaction(tx);

        TransactionEvent event = TransactionEvent.of(
                tx.id(),
                tx.creditAccountId(),
                tx.debitAccountId(),
                tx.amount(),
                tx.transactionType(),
                group.id()
        );

        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("transaction-created", json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish Kafka event", e);
        }
        return new TransactionCreated(tx.id(),group.id());
    }

    public record TransactionCreated(Long transactionId, Long groupId){}
}
