package com.corporatebanking.gateway.controller.transactions;

import com.corporatebanking.gateway.dto.transaction.CreateTransactionAccountTypeResponseDto;
import com.corporatebanking.gateway.dto.transaction.CreateTransactionRequestDto;
import com.corporatebanking.gateway.dto.transaction.CreateTransactionResponseDto;
import com.corporatebanking.transaction.grpc.CreateTransactionGrpc;
import com.corporatebanking.transaction.grpc.CreateTransactionRequest;
import com.corporatebanking.transaction.grpc.CreateTransactionResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/transactions")
public class CreateTransactionController {

    @GrpcClient("transaction-service")
    private CreateTransactionGrpc.CreateTransactionBlockingStub createTransactionStub;

    @PostMapping("/deposit")
    public ResponseEntity<CreateTransactionResponseDto> createTransaction(
            @RequestBody CreateTransactionRequestDto requestDto
    ) {
        CreateTransactionRequest request = CreateTransactionRequest.newBuilder()
                .setAccountTypeId(requestDto.accountTypeId())
                .setAccountNumber(requestDto.accountNumber())
                .setName(requestDto.name())
                .setAmount(requestDto.amount())
                .setNote(requestDto.note() != null ? requestDto.note() : "")
                .setCreatedAt(requestDto.createdAt() != null ? requestDto.createdAt() : "")
                .setUpdatedAt(requestDto.updatedAt() != null ? requestDto.updatedAt() : "")
                .build();

        CreateTransactionResponse response = createTransactionStub.createTransaction(request);
        CreateTransactionResponseDto responseDto = toDto(response);

        return ResponseEntity.ok(responseDto);
    }

    private CreateTransactionResponseDto toDto(CreateTransactionResponse t) {
        CreateTransactionAccountTypeResponseDto accountType = new CreateTransactionAccountTypeResponseDto(
                t.getAccountType().getId(),
                t.getAccountType().getName()
        );

        return new CreateTransactionResponseDto(
                t.getId(),
                accountType,
                t.getAccountNumber(),
                t.getName(),
                t.getAmount(),
                t.getNote(),
                t.getCreatedAt().isEmpty() ? null : LocalDate.parse(t.getCreatedAt()),
                t.getUpdatedAt().isEmpty() ? null : LocalDate.parse(t.getUpdatedAt())
        );
    }
}