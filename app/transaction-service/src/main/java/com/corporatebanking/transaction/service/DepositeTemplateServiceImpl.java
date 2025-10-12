package com.corporatebanking.transaction.service;

import java.util.List;
import java.util.stream.Collectors;
import com.corporatebanking.transaction.grpc.AccountTypeOption;
import com.corporatebanking.transaction.grpc.DepositTemplateResponse;
import com.corporatebanking.transaction.grpc.DepositTemplateServiceGrpc;
import com.corporatebanking.transaction.grpc.GetDepositTemplateRequest;
import com.corporatebanking.transaction.models.AccountTypeOptionData;
import com.corporatebanking.transaction.repository.jdbc.DepositeTemplateJdbcRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class DepositeTemplateServiceImpl extends DepositTemplateServiceGrpc.DepositTemplateServiceImplBase {

    private final DepositeTemplateJdbcRepository depositeTemplateJdbcRepository;

    public DepositeTemplateServiceImpl(DepositeTemplateJdbcRepository depositeTemplateJdbcRepository) {
        this.depositeTemplateJdbcRepository = depositeTemplateJdbcRepository;
    }

    @Override
    public void getDepositTemplate(GetDepositTemplateRequest request, StreamObserver<DepositTemplateResponse> responseObserver) {
        List<AccountTypeOptionData> accountTypeOptions = depositeTemplateJdbcRepository.findAllAccountTypeOptions();

        List<AccountTypeOption> accountTypeOptionsResponse = accountTypeOptions
            .stream()
            .map(this::toAccountTypeOption)
            .collect(Collectors.toList());
        
        DepositTemplateResponse response = DepositTemplateResponse
            .newBuilder()
            .addAllAccountTypeOptions(accountTypeOptionsResponse)
            .build();
            
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private AccountTypeOption toAccountTypeOption(AccountTypeOptionData data) {
        return AccountTypeOption
            .newBuilder()
            .setId(data.id())
            .setName(data.name())
            .build();
    }

}
