package com.corporatebanking.transaction.grpc;

import com.corporatebanking.transaction.service.TransactionService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

@GrpcService
public class TransactionServiceImpl extends com.corporatebanking.transaction.grpc.TransactionServiceGrpc.TransactionServiceImplBase {

    @Autowired
    private TransactionService transactionService;

    @Override
    public void createTransaction(com.corporatebanking.transaction.grpc.CreateTransactionRequest request,
                                  StreamObserver<com.corporatebanking.transaction.grpc.CreateTransactionResponse> responseObserver) {
        try {
            Long groupId = (request.getTransactionGroupId() == 0) ? null : request.getTransactionGroupId();

            BigDecimal amount = new BigDecimal(request.getAmount());

            var result = transactionService.createTransaction(
                    request.getCreditAccountId(),
                    request.getDebitAccountId(),
                    amount,
                    request.getTransactionType(),
                    groupId
            );

            var response = com.corporatebanking.transaction.grpc.CreateTransactionResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Transaction created successfully")
                    .setTransactionId(result.transactionId())
                    .setTransactionGroupId(result.groupId())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (NumberFormatException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid amount format: " + e.getMessage())
                    .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }

    }
}
