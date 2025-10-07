package com.corporatebanking.user.features.accountchangestatus.service;

import com.corporatebanking.accountchangestatus.grpc.AccountChangeStatusRequest;
import com.corporatebanking.accountchangestatus.grpc.AccountChangeStatusResponse;
import com.corporatebanking.accountchangestatus.grpc.AccountServiceGrpc;
import com.corporatebanking.user.features.accountchangestatus.model.Account;
import com.corporatebanking.user.features.accountchangestatus.repository.impl.AccountRepositoryImpl;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
public class AccountChangeStatusServiceImpl extends AccountServiceGrpc.AccountServiceImplBase {

    @Autowired
    private AccountRepositoryImpl accountRepositoryImpl;

    @Override
    public void accountChangeStatusGrpc(AccountChangeStatusRequest request, StreamObserver<AccountChangeStatusResponse> responseObserver) {
        try {
            Long accountId = request.getAccountId();
            String newStatus = request.getNewStatus().toUpperCase();

            if (!isValidStatus(newStatus)) {
                sendError(responseObserver, "Invalid status: " + newStatus, "VALIDATION_ERROR");
                return;
            }

            Optional<Account> existingAccount = accountRepositoryImpl.findById(accountId);
//                    .orElseThrow(() -> new RuntimeException("Account with ID " + accountId + " not found"));

            if(existingAccount.isEmpty()){
                sendError(responseObserver, "Account with id " + accountId + " does not exist", "NOT_FOUND");
                return;
            }
            Account existingAccountObj = existingAccount.get();

            if (existingAccountObj.status().equalsIgnoreCase(newStatus)) {
                sendError(responseObserver, "Account with ID " + accountId + " already has status '" + existingAccountObj.status() + "'.", "DATA_CONFLICT");
                return;
            }

            accountRepositoryImpl.updateStatus(accountId, newStatus);

            AccountChangeStatusResponse response = AccountChangeStatusResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Account status updated to " + newStatus)
                    .setErrorCode("SUCCESS")
                    .build();

            responseObserver.onNext(response);
        } catch (Exception e) {
            sendError(responseObserver, "Failed to update status: " + e.getMessage(), "DB_ERROR");
        } finally {
            responseObserver.onCompleted();
        }
    }

    private void sendError(StreamObserver<AccountChangeStatusResponse> responseObserver, String message, String errorCode) {
        AccountChangeStatusResponse response = AccountChangeStatusResponse.newBuilder()
                .setSuccess(false)
                .setMessage(message)
                .setErrorCode(errorCode)
                .build();
        responseObserver.onNext(response);
    }


    private boolean isValidStatus(String status) {
        return switch (status) {
            case "ACTIVE", "INACTIVE", "SUSPENDED" -> true;
            default -> false;
        };
    }
}