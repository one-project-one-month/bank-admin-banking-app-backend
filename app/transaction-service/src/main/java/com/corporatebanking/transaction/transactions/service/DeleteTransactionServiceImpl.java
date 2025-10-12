package com.corporatebanking.transaction.transactions.service;

import com.corporatebanking.transaction.grpc.DeleteTransactionRequest;
import com.corporatebanking.transaction.grpc.DeleteTransactionResponse;
import com.corporatebanking.transaction.grpc.TransactionServiceGrpc;
import com.corporatebanking.transaction.transactions.repository.DeleteTransactionJdbcRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional; // <-- Required for commit

@GrpcService
public class DeleteTransactionServiceImpl extends TransactionServiceGrpc.TransactionServiceImplBase {

    private final DeleteTransactionJdbcRepository deleteTransactionJdbcRepository;

    @Autowired
    public DeleteTransactionServiceImpl(DeleteTransactionJdbcRepository deleteTransactionJdbcRepository) {
        this.deleteTransactionJdbcRepository = deleteTransactionJdbcRepository;
    }

    @Override
    @Transactional // 2. Add @Transactional to ensure the deletion is committed to the database
    public void deleteTransaction(DeleteTransactionRequest request,
                                  StreamObserver<DeleteTransactionResponse> responseObserver) {

        long transactionId = request.getId();
        boolean success;
        String message;

        if (transactionId <= 0) {
            success = false;
            message = "Invalid transaction ID provided: " + transactionId;
        } else {
            try {

                deleteTransactionJdbcRepository.deleteById(transactionId);

                success = true;
                message = "Transaction deleted successfully.";

            } catch (Exception e) {
                success = false;
                message = "Failed to delete transaction ID " + transactionId + ". Database error occurred.";
            }
        }

        DeleteTransactionResponse response = DeleteTransactionResponse.newBuilder()
                .setSuccess(success)
                .setMessage(message)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
