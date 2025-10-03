package com.corporatebanking.gateway.controller.transactions;

import com.corporatebanking.gateway.dto.transaction.AccountTypeResponseDto;
import com.corporatebanking.gateway.dto.transaction.CreateTransactionRequestDto;
import com.corporatebanking.gateway.dto.transaction.TransactionResponseDto;
import com.corporatebanking.transaction.CreateTransactionGrpc;
import com.corporatebanking.transaction.CreateTransactionRequest;
import com.corporatebanking.transaction.TransactionResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @GrpcClient("transaction-service")
    private CreateTransactionGrpc.CreateTransactionBlockingStub transactionStub;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody CreateTransactionRequestDto dto) {

        CreateTransactionRequest grpcRequest = CreateTransactionRequest.newBuilder()
                .setAccountTypeId(dto.accountTypeId())
                .setAccountNumber(dto.accountNumber())
                .setName(dto.name())
                .setAmount(dto.amount())
                .setNote(dto.note() == null ? "" : dto.note())
                .build();

        TransactionResponse grpcResponse = transactionStub.create(grpcRequest);


        return  ResponseEntity.ok(toDto(grpcResponse));
    }

    private TransactionResponseDto toDto(TransactionResponse response) {
        return new TransactionResponseDto(
                response.getId(),
                new AccountTypeResponseDto(
                        response.getAccountType().getId(),
                        response.getAccountType().getName()
                ),
                response.getAccountNumber(),
                response.getName(),
                response.getAmount(),
                response.getNote(),
                response.getCreatedAt().isBlank() ? null : LocalDate.parse(response.getCreatedAt()),
                response.getUpdatedAt().isBlank() ? null : LocalDate.parse(response.getUpdatedAt())
        );
    }

}
