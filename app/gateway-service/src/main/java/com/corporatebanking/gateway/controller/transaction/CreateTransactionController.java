package com.corporatebanking.gateway.controller.transaction;

import com.corporatebanking.gateway.dto.transaction.CreateTransactionRequestDto;
import com.corporatebanking.gateway.dto.transaction.CreateTransactionResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/transaction")
public class CreateTransactionController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @GrpcClient("transaction-service")
    private com.corporatebanking.transaction.proto.TransactionServiceGrpc.TransactionServiceBlockingStub transactionServiceBlockingStub;

    public  CreateTransactionController(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<CreateTransactionResponseDto> createTransaction(@RequestBody CreateTransactionRequestDto request) throws JsonProcessingException {
        com.corporatebanking.transaction.proto.CreateTransactionRequest createTransactionRequest = com.corporatebanking.transaction.proto.CreateTransactionRequest.newBuilder()
                .setCreditAccountId(request.creditAccountId())
                .setDebitAccountId(request.debitAccoutnId())
                .setAmount(request.amount().toString())
                .setTransactionType(request.transactionType())
                .setTransactionGroupId(request.transactionGroupId())
                .build();

        String message = objectMapper.writeValueAsString(request);
        kafkaTemplate.send("create-transaction-topic", message);


        com.corporatebanking.transaction.proto.CreateTransactionResponse createTransactionResponse = transactionServiceBlockingStub.createTransaction(createTransactionRequest);
        return ResponseEntity.ok(toDto(createTransactionResponse));
    }

    private CreateTransactionResponseDto toDto(com.corporatebanking.transaction.proto.CreateTransactionResponse createTransactionResponse) {
        return new CreateTransactionResponseDto(
                createTransactionResponse.getSuccess(),
                createTransactionResponse.getMessage(),
                createTransactionResponse.getTransactionId(),
                createTransactionResponse.getTransactionGroupId()
        );
    }
}
