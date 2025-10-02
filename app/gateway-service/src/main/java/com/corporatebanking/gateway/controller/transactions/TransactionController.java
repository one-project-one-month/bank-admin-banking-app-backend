package com.corporatebanking.gateway.controller.transactions;

import com.corporatebanking.gateway.dto.transaction.AccountTypeResponseDto;
import com.corporatebanking.gateway.dto.transaction.TransactionResponseDto;
import com.corporatebanking.transaction.CreateTransactionGrpc;
import com.corporatebanking.transaction.CreateTransactionRequest;
import com.corporatebanking.transaction.TransactionResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @GrpcClient("transaction-service")
    private CreateTransactionGrpc.CreateTransactionBlockingStub transactionStub;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody CreateTransactionRequest grpcRequest) {

        TransactionResponse grpcResponse = transactionStub.create(grpcRequest);

        TransactionResponseDto dto = toDto(grpcResponse);

        return  ResponseEntity.ok(dto);
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
