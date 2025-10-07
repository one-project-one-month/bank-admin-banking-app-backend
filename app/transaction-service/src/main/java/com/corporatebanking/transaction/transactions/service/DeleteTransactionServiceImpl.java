package com.corporatebanking.transaction.transactions.service;
import com.corporatebanking.transaction.grpc.DeleteTransactionRequest;
import com.corporatebanking.transaction.grpc.DeleteTransactionResponse;
import com.corporatebanking.transaction.grpc.TransactionServiceGrpc;
//import com.corporatebanking.transaction.transactions.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;


// Import generated gRPC/Proto classes

@GrpcService
public class DeleteTransactionServiceImpl extends TransactionServiceGrpc.TransactionServiceImplBase {
    @Override
    public void deleteTransaction(DeleteTransactionRequest request,
                                  StreamObserver<DeleteTransactionResponse> responseObserver) {

        boolean success = request.getId() > 0;
        String message = success ?
                "Transaction ID " + request.getId() + " deleted successfully" :
                "Transaction not found or invalid ID: " + request.getId();

        DeleteTransactionResponse response = DeleteTransactionResponse.newBuilder()
                .setSuccess(success)
                .setMessage(message)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}