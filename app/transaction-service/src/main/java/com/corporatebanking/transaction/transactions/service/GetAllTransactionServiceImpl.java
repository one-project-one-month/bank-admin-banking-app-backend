package com.corporatebanking.transaction.transactions.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corporatebanking.transaction.grpc.*;
import com.corporatebanking.transaction.transactions.models.GetAllTransactionData;
import com.corporatebanking.transaction.transactions.models.GetAllAccountTypeData;
import com.corporatebanking.transaction.transactions.repository.jdbc.GetAllTransactionJdbcRepository;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GetAllTransactionServiceImpl extends GetAllTransactionServiceGrpc.GetAllTransactionServiceImplBase {
    private final GetAllTransactionJdbcRepository transactionJdbcRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
   
    public GetAllTransactionServiceImpl(GetAllTransactionJdbcRepository transactionJdbcRepository) {
        this.transactionJdbcRepository = transactionJdbcRepository;
    }

    @Override
    public void getAllTransactions(GetAllTransactionsRequest request, StreamObserver<GetAllTransactionListResponse> responseObserver) {
        List<GetAllTransactionData> transactions = transactionJdbcRepository.findAll();
        List<GetAllTransactionsResponse> transactionResponses = transactions.stream()
                .map(this::toTransactionResponse)
                .collect(Collectors.toList());

        GetAllTransactionListResponse response = GetAllTransactionListResponse.newBuilder()
                .addAllTransactions(transactionResponses)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    private GetAllTransactionsResponse toTransactionResponse(GetAllTransactionData transactionData) {
        // Convert AccountTypeData to gRPC AccountType
        GetAllAccountType accountType = GetAllAccountType.newBuilder()
                .setId(transactionData.accountType().id())
                .setName(transactionData.accountType().name())
                .build();

        return GetAllTransactionsResponse.newBuilder()
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