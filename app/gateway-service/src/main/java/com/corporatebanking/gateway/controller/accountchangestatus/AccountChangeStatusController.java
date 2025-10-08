package com.corporatebanking.gateway.controller.accountchangestatus;

import com.corporatebanking.accountchangestatus.grpc.AccountChangeStatusRequest;
import com.corporatebanking.accountchangestatus.grpc.AccountChangeStatusResponse;
import com.corporatebanking.accountchangestatus.grpc.AccountServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/accounts")
public class AccountChangeStatusController {

    // gRPC client for user service
    @GrpcClient("user-service")
    private AccountServiceGrpc.AccountServiceBlockingStub accountServiceStub;


    @PatchMapping("/{id}/change-status")
    public ResponseEntity<Map<String, Object>> changeAccountStatus(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> requestBody
    ){
        String newStatus = requestBody.get("newStatus");

        if (newStatus == null || newStatus.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "newStatus is required"));
        }

        AccountChangeStatusRequest grpcRequest = AccountChangeStatusRequest.newBuilder()
                .setAccountId(id)
                .setNewStatus(newStatus)
                .build();

        AccountChangeStatusResponse grpcResponse = accountServiceStub.accountChangeStatusGrpc(grpcRequest);

        HttpStatus status = switch (grpcResponse.getErrorCode()) {
            case "VALIDATION_ERROR" -> HttpStatus.BAD_REQUEST;
            case "NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "DATA_CONFLICT" -> HttpStatus.CONFLICT;
            case "DB_ERROR" -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.OK;
        };

        Map<String, Object> response = Map.of(
                "code", status.value(),
                "message", grpcResponse.getMessage()
        );

        return ResponseEntity.status(status).body(response);
    }

}
