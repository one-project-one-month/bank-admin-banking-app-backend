package com.corporatebanking.gateway.controller.transactions;
import com.corporatebanking.gateway.dto.transaction.AccountTypeResponseDto;
import com.corporatebanking.gateway.dto.transaction.TransactionResponseDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.devh.boot.grpc.client.inject.GrpcClient;
import com.corporatebanking.transaction.grpc.TransactionServiceGrpc;
import com.corporatebanking.transaction.grpc.GetAllTransactionsRequest;
import com.corporatebanking.transaction.grpc.TransactionListResponse;
import com.corporatebanking.transaction.grpc.TransactionResponse;
import com.corporatebanking.transaction.grpc.AccountType;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    @GrpcClient("transaction-service")
    private TransactionServiceGrpc.TransactionServiceBlockingStub transactionServiceStub;

    @GetMapping("/deposit")
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactions() {
        GetAllTransactionsRequest request = GetAllTransactionsRequest.newBuilder().build();
        TransactionListResponse response = transactionServiceStub.getAllTransactions(request);
        List<TransactionResponseDto> dtos = response.getTransactionsList().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    
    private TransactionResponseDto toDto(TransactionResponse t) {
        // Get the first account type from the repeated field, or null if empty
        AccountTypeResponseDto accountType = null;
        if (!t.getAccountTypeList().isEmpty()) {
            AccountType firstAccountType = t.getAccountTypeList().get(0);
            accountType = new AccountTypeResponseDto(
                firstAccountType.getId(),
                firstAccountType.getName()
            );
        }
        
        return new TransactionResponseDto(
            t.getId(),
            accountType,
            t.getAccountNumber(),
            t.getName(),
            t.getAmount(),
            t.getNote(),
            t.getCreatedAt().isEmpty() ? null : LocalDate.parse(t.getCreatedAt()),
            t.getUpdatedAt().isEmpty() ? null : LocalDate.parse(t.getUpdatedAt())
        );
    }
    
}
