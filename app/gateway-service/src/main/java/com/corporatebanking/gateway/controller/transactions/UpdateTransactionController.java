package com.corporatebanking.gateway.controller.transactions;

import com.corporatebanking.gateway.dto.transaction.AccountTypeDto;
import com.corporatebanking.gateway.dto.transaction.DepositTranxDto;
import com.corporatebanking.gateway.dto.transaction.DepositTranxResponse;
import com.corporatebanking.gateway.dto.transaction.DepositTrnxRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/deposit")
public class UpdateTransactionController {

    @GrpcClient("transaction-service")
    private com.corporatebanking.transaction.grpc.UpdateDepositTranxGrpc.UpdateDepositTranxBlockingStub updateDepositTranxStub;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepositTransaction(
            @PathVariable long id,
            @RequestBody DepositTrnxRequest depositTrnxRequest
            ) {
        com.corporatebanking.transaction.grpc.DepositTranxRequest depositTranxRequest = com.corporatebanking.transaction.grpc.DepositTranxRequest.newBuilder()
                .setAccountTypeId(depositTrnxRequest.accountTypeId())
                .setAccountNumber(depositTrnxRequest.accountNumber())
                .setName(depositTrnxRequest.name())
                .setAmount(depositTrnxRequest.amount())
                .build();

        com.corporatebanking.transaction.grpc.UpdateDepositTrnxRequest request = com.corporatebanking.transaction.grpc.UpdateDepositTrnxRequest.newBuilder()
                .setTransactionId(id)
                .setDepositTranxRequest(depositTranxRequest)
                .build();

        com.corporatebanking.transaction.grpc.DepositTranxResponse response = updateDepositTranxStub.updateDepositTranx(request);
        DepositTranxResponse<DepositTranxDto> responseDto = new DepositTranxResponse<>(
                response.getStatusCode(),
                response.getMessage(),
                toDto(response)
        );
        return ResponseEntity.ok(responseDto);
    }

    private DepositTranxDto toDto(com.corporatebanking.transaction.grpc.DepositTranxResponse response) {

        if(!response.hasTransaction()) return null;

        AccountTypeDto accountTypeDto = new AccountTypeDto(
                response.getTransaction().getId(),
                response.getTransaction().getName()
        );

        return new DepositTranxDto(
                response.getTransaction().getId(),
                accountTypeDto,
                response.getTransaction().getAccountNumber(),
                response.getTransaction().getName(),
                response.getTransaction().getAmount(),
                response.getTransaction().getNote(),
                parseDate(response.getTransaction().getCreatedAt()),
                parseDate(response.getTransaction().getUpdatedAt())
        );
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            return null;
        }
    }
}
