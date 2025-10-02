package com.corporatebanking.transaction.transactions.service;

import com.corporatebanking.transaction.transactions.models.AccountTypeData;
import com.corporatebanking.transaction.transactions.models.TransactionData;
import com.corporatebanking.transaction.transactions.repository.jdbc.TransactionJdbcRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import java.time.format.DateTimeFormatter;

public class TransactionServiceImpl extends com.corporatebanking.transaction.grpc.CreateTransactionGrpc.CreateTransactionImplBase {
    private final TransactionJdbcRepository transactionJdbcRepository;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public TransactionServiceImpl(TransactionJdbcRepository transactionJdbcRepository) {
        this.transactionJdbcRepository = transactionJdbcRepository;
    }

    @Override
    public void create(com.corporatebanking.transaction.grpc.CreateTransactionRequest request,
                                  StreamObserver<com.corporatebanking.transaction.grpc.TransactionResponse> responseObserver) {
        try {
            TransactionData toSave = new TransactionData(
                    null,
                    new AccountTypeData(request.getAccountTypeId(), null),
                    request.getAccountNumber(),
                    request.getName(),
                    request.getAmount(),
                    request.getNote(),
                    null,
                    null
            );

            TransactionData saved = transactionJdbcRepository.save(toSave);

            com.corporatebanking.transaction.grpc.TransactionResponse response = com.corporatebanking.transaction.grpc.TransactionResponse.newBuilder()
                    .setId(saved.id())
                    .setAccountType(
                            com.corporatebanking.transaction.grpc.AccountTypeData.newBuilder()
                                    .setId(saved.accountType() != null ? saved.accountType().id() : 0L)
                                    .setName(saved.accountNumber() != null && saved.accountType().name() != null
                                            ? saved.accountType().name() : "")
                                    .build()
                    )
                    .setAccountNumber(saved.accountNumber())
                    .setName(saved.name())
                    .setNote(saved.note() == null ? "" : saved.note())
                    .setCreatedAt(saved.createdAt() != null ? dateTimeFormatter.format(saved.createdAt()) : "")
                    .setUpdatedAt(saved.updatedAt() != null ? dateTimeFormatter.format(saved.updatedAt()) : "")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (DuplicateKeyException e) {
            responseObserver.onError(Status.ALREADY_EXISTS
                    .withDescription("Account number already exists: " + e.getMostSpecificCause().getMessage())
                    .asRuntimeException());
        } catch (DataIntegrityViolationException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid transaction data: " + e.getMostSpecificCause().getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to create transaction")
                    .withCause(e)
                    .asRuntimeException());
        }
    }
}
