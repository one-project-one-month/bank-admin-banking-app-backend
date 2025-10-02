package com.corporatebanking.transaction.service;

import com.corporatebanking.transaction.model.Transaction;
import com.corporatebanking.transaction.repository.jdbc.TransactionJdbcRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@GrpcService
public class TransactionServiceImpl extends com.corporatebanking.transaction.proto.TransactionServiceGrpc.TransactionServiceImplBase {

    @Autowired
    private TransactionJdbcRepository transactionJdbcRepository;

    @Override
    public void createTransaction(com.corporatebanking.transaction.proto.CreateTransactionRequest request,
                                  StreamObserver<com.corporatebanking.transaction.proto.CreateTransactionResponse> responseStreamObserver) {
        try{
            if (request.getAmount() == null || request.getAmount().isEmpty()){
                throw new IllegalArgumentException("Amount is required");
            }
            BigDecimal amount = new BigDecimal(request.getAmount());
            if (amount.compareTo(BigDecimal.ZERO) <= 0){
                throw new IllegalArgumentException("Amount must be greate than zero");
            }

            Long groupId = (request.getTransactionGroupId() == 0) ? null : request.getTransactionGroupId();

            Transaction tx = new Transaction(
              null,
              request.getCreditAccountId(),
              request.getDebitAccountId(),
              amount,
              request.getTransactionType(),
              groupId
            );

            tx = transactionJdbcRepository.save(tx);

            var response = com.corporatebanking.transaction.proto.CreateTransactionResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Transaction created")
                    .setTransactionId(tx.id())
                    .setTransactionId(tx.transactionGroupId() != null ? tx.transactionGroupId() : 0L)
                    .build();

            responseStreamObserver.onNext(response);
            responseStreamObserver.onCompleted();

        } catch (Exception e){
            responseStreamObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException()
            );
        }
    }
}
