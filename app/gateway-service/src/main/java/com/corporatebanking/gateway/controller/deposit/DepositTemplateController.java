package com.corporatebanking.gateway.controller.deposit;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.corporatebanking.gateway.dto.deposit.AccountTypeOptionDto;
import com.corporatebanking.gateway.dto.deposit.DepositTemplateResponseDto;
import com.corporatebanking.transaction.grpc.DepositTemplateResponse;
import com.corporatebanking.transaction.grpc.DepositTemplateServiceGrpc;
import com.corporatebanking.transaction.grpc.GetDepositTemplateRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;

@RestController
@RequestMapping("/api/v1/deposit")
public class DepositTemplateController {

    @GrpcClient("transaction-service")
    private DepositTemplateServiceGrpc.DepositTemplateServiceBlockingStub depositeTemplateServiceStub;

    @GetMapping("/template")
    public ResponseEntity<DepositTemplateResponseDto> getDepositeTemplate() {
        GetDepositTemplateRequest request = GetDepositTemplateRequest.newBuilder().build();
        DepositTemplateResponse response = depositeTemplateServiceStub.getDepositTemplate(request);
        List<AccountTypeOptionDto> accountTypeOptionDtos = response.getAccountTypeOptionsList()
            .stream()
            .map(data -> {
                return new AccountTypeOptionDto(data.getId(), data.getName());
            })
            .collect(Collectors.toList());
        DepositTemplateResponseDto dtos = new DepositTemplateResponseDto(accountTypeOptionDtos);
        return ResponseEntity.ok(dtos);
    }
 
}
