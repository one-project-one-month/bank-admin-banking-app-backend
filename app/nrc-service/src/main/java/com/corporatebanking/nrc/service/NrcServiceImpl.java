package com.corporatebanking.nrc.service;

import com.corporatebanking.nrc.grpc.*;
import com.corporatebanking.nrc.model.NrcCode;
import com.corporatebanking.nrc.model.NrcCodeValue;
import com.corporatebanking.nrc.repository.NrcRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@GrpcService
public class NrcServiceImpl extends NrcServiceGrpc.NrcServiceImplBase {

    @Autowired
    private NrcRepository nrcRepository;

    @Override
    public void getAllNrcCodes(GetAllNrcCodesRequest request, StreamObserver<NrcCodeListResponse> responseObserver) {
        var codes = nrcRepository.findAllNrcCodes().stream()
                .map(this::toNrcCodeResponse)
                .collect(Collectors.toList());
        responseObserver.onNext(NrcCodeListResponse.newBuilder().addAllCodes(codes).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getNrcValuesByCodeName(GetNrcValuesByCodeNameRequest request, StreamObserver<NrcCodeValueListResponse> responseObserver) {
        var values = nrcRepository.findValuesByCodeName(request.getCodeName()).stream()
                .map(this::toNrcCodeValueResponse)
                .collect(Collectors.toList());
        responseObserver.onNext(NrcCodeValueListResponse.newBuilder().addAllValues(values).build());
        responseObserver.onCompleted();
    }

    private NrcCodeResponse toNrcCodeResponse(NrcCode code) {
        return NrcCodeResponse.newBuilder()
                .setId(code.id())
                .setName(code.name())
                .build();
    }

    private NrcCodeValueResponse toNrcCodeValueResponse(NrcCodeValue value) {
        return NrcCodeValueResponse.newBuilder()
                .setId(value.id())
                .setCodeId(value.codeId())
                .setValue(value.value())
                .build();
    }
}
