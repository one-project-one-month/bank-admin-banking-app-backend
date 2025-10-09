package com.corporatebanking.gateway.controller.transactions;
import com.corporatebanking.transaction.grpc.DeleteTransactionRequest;
import com.corporatebanking.transaction.grpc.DeleteTransactionResponse;
import com.corporatebanking.transaction.grpc.TransactionServiceGrpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/transactions")
public class DeleteTransactionController {

    @GrpcClient("transaction-service")
    private TransactionServiceGrpc.TransactionServiceBlockingStub transactionStub;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        DeleteTransactionRequest request = DeleteTransactionRequest.newBuilder()
                .setId(id)
                .build();

        DeleteTransactionResponse response = transactionStub.deleteTransaction(request);

        if (response.getSuccess()) {
            return ResponseEntity.ok("Transaction deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found");
        }
    }
}
