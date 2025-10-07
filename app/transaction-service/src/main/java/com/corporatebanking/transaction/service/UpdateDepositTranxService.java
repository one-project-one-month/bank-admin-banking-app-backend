package com.corporatebanking.transaction.service;

import com.corporatebanking.transaction.grpc.UpdateDepositTrnxRequest;
import com.corporatebanking.transaction.model.DepositTranxDto;
import com.corporatebanking.transaction.model.DepositTranxRequest;
import com.corporatebanking.transaction.repository.jdbc.impl.DepositTranxJdbcRepositoryImpl;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
@GrpcService
public class UpdateDepositTranxService extends com.corporatebanking.transaction.grpc.UpdateDepositTranxGrpc.UpdateDepositTranxImplBase {

    private final DepositTranxJdbcRepositoryImpl repository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public UpdateDepositTranxService(DepositTranxJdbcRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public void updateDepositTranx(UpdateDepositTrnxRequest request, StreamObserver<com.corporatebanking.transaction.grpc.DepositTranxResponse> responseObserver) {
        long id = request.getTransactionId();
        DepositTranxRequest requestDepositData = toDepositTranxRequest(request);

        int isUpdated = repository.update(id, requestDepositData);

        if(isUpdated==0) {
            com.corporatebanking.transaction.grpc.DepositTranxResponse response = com.corporatebanking.transaction.grpc.DepositTranxResponse.newBuilder()
                    .setStatusCode(403)
                    .setMessage("Forbidden")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            com.corporatebanking.transaction.grpc.DepositTranxResponse response = toResponse(
                    200,
                    "Deposit transaction retrieved successfully",
                    requestDepositData,
                    id);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

    }

    private com.corporatebanking.transaction.grpc.DepositTranxResponse toResponse(int code, String message, DepositTranxRequest requestDepositData, Long id) {

        DepositTranxDto updatedDeposit = repository.getDepositTranxById(id);

        com.corporatebanking.transaction.grpc.AccountTypeDto accountType = com.corporatebanking.transaction.grpc.AccountTypeDto.newBuilder()
                .setId(updatedDeposit.accountType().id())
                .setName(updatedDeposit.accountType().name())
                .build();

        com.corporatebanking.transaction.grpc.DepositTranxDto depositTranxDto = com.corporatebanking.transaction.grpc.DepositTranxDto.newBuilder()
                .setId(updatedDeposit.id())
                .setAccountType(accountType)
                .setAccountNumber(updatedDeposit.accountNumber())
                .setName(updatedDeposit.name())
                .setAmount(updatedDeposit.amount())
                .setNote(updatedDeposit.note())
                .setCreatedAt(updatedDeposit.createdAt().format(dateFormatter))
                .setUpdatedAt(LocalDate.now().format(dateFormatter))
                .build();

        return com.corporatebanking.transaction.grpc.DepositTranxResponse.newBuilder()
                .setStatusCode(code)
                .setMessage(message)
                .setTransaction(depositTranxDto)
                .build();
    }

    private DepositTranxRequest toDepositTranxRequest(UpdateDepositTrnxRequest request) {

        com.corporatebanking.transaction.grpc.DepositTranxRequestOrBuilder builder = request.getDepositTranxRequestOrBuilder();

        return new DepositTranxRequest(
                builder.getAccountTypeId(),
                builder.getAccountNumber(),
                builder.getName(),
                builder.getAmount(),
                builder.getNote()
        );
    }
}
