package com.corporatebanking.transaction.transactions.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corporatebanking.transaction.grpc.*;
import com.corporatebanking.transaction.transactions.models.TransactionData;
import com.corporatebanking.transaction.transactions.models.AccountTypeData;
import com.corporatebanking.transaction.transactions.repository.jdbc.TransactionJdbcRepository;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class TransactionServiceImpl extends TransactionServiceGrpc.TransactionServiceImplBase {
    private final TransactionJdbcRepository transactionJdbcRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
   
    public TransactionServiceImpl(TransactionJdbcRepository transactionJdbcRepository) {
        this.transactionJdbcRepository = transactionJdbcRepository;
    }

    @Override
    public void getAllTransactions(GetAllTransactionsRequest request, StreamObserver<TransactionListResponse> responseObserver) {
        List<TransactionData> transactions = transactionJdbcRepository.findAll();
        List<TransactionResponse> transactionResponses = transactions.stream()
                .map(this::toTransactionResponse)
                .collect(Collectors.toList());

        TransactionListResponse response = TransactionListResponse.newBuilder()
                .addAllTransactions(transactionResponses)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    private TransactionResponse toTransactionResponse(TransactionData transactionData) {
        // Convert AccountTypeData to gRPC AccountType
        AccountType accountType = AccountType.newBuilder()
                .setId(transactionData.accountType().id())
                .setName(transactionData.accountType().name())
                .build();
        
        return TransactionResponse.newBuilder()
                .setId(transactionData.id())
                .addAccountType(accountType)
                .setAccountNumber(transactionData.accountNumber())
                .setName(transactionData.name())
                .setAmount(transactionData.amount())
                .setNote(transactionData.note())
                .setCreatedAt(transactionData.createdAt().format(dateFormatter))
                .setUpdatedAt(transactionData.updatedAt().format(dateFormatter))
                .build();
    }
}