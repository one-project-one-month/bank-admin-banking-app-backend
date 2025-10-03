package com.corporatebanking.transaction.transactions.service;

import com.corporatebanking.transaction.transactions.models.AccountTypeData;
import com.corporatebanking.transaction.transactions.models.TransactionData;
import com.corporatebanking.transaction.transactions.repository.jdbc.TransactionJdbcRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@GrpcService
public class TransactionServiceImpl extends com.corporatebanking.transaction.grpc.CreateTransactionGrpc.CreateTransactionImplBase {
    private final TransactionJdbcRepository transactionJdbcRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public TransactionServiceImpl(TransactionJdbcRepository transactionJdbcRepository) {
        this.transactionJdbcRepository = transactionJdbcRepository;
    }

    @Override
    public void create(com.corporatebanking.transaction.grpc.CreateTransactionRequest request,
                                  StreamObserver<com.corporatebanking.transaction.grpc.TransactionResponse> responseObserver) {
        try {

            TransactionData transactionData = toTransactionData(request);

            TransactionData savedTransaction = transactionJdbcRepository.save(transactionData);

            com.corporatebanking.transaction.grpc.TransactionResponse response = toTransactionResponse(savedTransaction);

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IllegalArgumentException e) {
            responseObserver.onError(io.grpc.Status.INVALID_ARGUMENT
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Fail to create transaction: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    private TransactionData toTransactionData(com.corporatebanking.transaction.grpc.CreateTransactionRequest request) {
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (request.getAccountTypeId() <= 0) {
            throw new IllegalArgumentException("Invalid account type");
        }

        if (request.getAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("Account number is required");
        }

        LocalDate createdAt = request.getCreatedAt().isEmpty()
                ? LocalDate.now()
                : LocalDate.parse(request.getCreatedAt(), dateFormatter);
        LocalDate updateAt = request.getUpdatedAt().isEmpty()
                ? LocalDate.now()
                : LocalDate.parse(request.getUpdatedAt(), dateFormatter);

        AccountTypeData accountTypeData = new AccountTypeData(
                request.getAccountTypeId(),
                "unknown"
        );

        return new TransactionData(
                null,
                accountTypeData,
                request.getAccountNumber(),
                request.getName(),
                request.getAmount(),
                request.getNote(),
                createdAt,
                updateAt
        );
    }

    private com.corporatebanking.transaction.grpc.TransactionResponse toTransactionResponse(TransactionData transactionData) {
        com.corporatebanking.transaction.grpc.AccountTypeData accountType = com.corporatebanking.transaction.grpc.AccountTypeData.newBuilder()
                .setId(transactionData.accountType().id())
                .setName(transactionData.accountType().name())
                .build();

        return com.corporatebanking.transaction.grpc.TransactionResponse.newBuilder()
                .setId(transactionData.id())
                .setAccountType(accountType)
                .setAccountNumber(transactionData.accountNumber())
                .setName(transactionData.name())
                .setAmount(transactionData.amount())
                .setNote(transactionData.note())
                .setCreatedAt(transactionData.createdAt().format(dateFormatter))
                .setUpdatedAt(transactionData.createdAt().format(dateFormatter))
                .build();
    }

}
