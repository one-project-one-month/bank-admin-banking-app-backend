package com.corporatebanking.transaction.transactions.service;

import com.corporatebanking.transaction.transactions.models.CreateTransactionAccountTypeData;
import com.corporatebanking.transaction.transactions.models.CreateTransactionData;
import com.corporatebanking.transaction.transactions.repository.jdbc.CreateTransactionJdbcRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@GrpcService
public class CreateTransactionServiceImpl extends com.corporatebanking.transaction.grpc.CreateTransactionGrpc.CreateTransactionImplBase {
    private final CreateTransactionJdbcRepository createTransactionJdbcRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public CreateTransactionServiceImpl(CreateTransactionJdbcRepository createTransactionJdbcRepository) {
        this.createTransactionJdbcRepository = createTransactionJdbcRepository;
    }

    @Override
    public void createTransaction(com.corporatebanking.transaction.grpc.CreateTransactionRequest request,
                                  StreamObserver<com.corporatebanking.transaction.grpc.CreateTransactionResponse> responseObserver) {
        try {

            CreateTransactionData createTransactionData = toCreateTransactionData(request);

            CreateTransactionData savedTransaction = createTransactionJdbcRepository.save(createTransactionData);

            com.corporatebanking.transaction.grpc.CreateTransactionResponse response = toCreateTransactionResponse(savedTransaction);

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

    private CreateTransactionData toCreateTransactionData(com.corporatebanking.transaction.grpc.CreateTransactionRequest request) {
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

        CreateTransactionAccountTypeData accountTypeData = new CreateTransactionAccountTypeData(
                request.getAccountTypeId(),
                "unknown"
        );

        return new CreateTransactionData(
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

    private com.corporatebanking.transaction.grpc.CreateTransactionResponse toCreateTransactionResponse(CreateTransactionData createTransactionData) {
        com.corporatebanking.transaction.grpc.CreateTransactionAccountTypeData accountType =
                com.corporatebanking.transaction.grpc.CreateTransactionAccountTypeData.newBuilder()
                        .setId(createTransactionData.accountType().id())
                        .setName(createTransactionData.accountType().name())
                        .build();

        return com.corporatebanking.transaction.grpc.CreateTransactionResponse.newBuilder()
                .setId(createTransactionData.id())
                .setAccountType(accountType)
                .setAccountNumber(createTransactionData.accountNumber())
                .setName(createTransactionData.name())
                .setAmount(createTransactionData.amount())
                .setNote(createTransactionData.note())
                .setCreatedAt(createTransactionData.createdAt().format(dateFormatter))
                .setUpdatedAt(createTransactionData.createdAt().format(dateFormatter))
                .build();
    }
}