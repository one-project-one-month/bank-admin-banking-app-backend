package com.corporatebanking.user.features.nrc.service;

import com.corporatebanking.nrc.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class NrcServiceImpl extends NrcServiceGrpc.NrcServiceImplBase {

    @GrpcClient("nrc-service")
    private NrcServiceGrpc.NrcServiceBlockingStub nrcServiceBlockingStub;

    @Override
    public void getAllNrcCodes(GetAllNrcCodesRequest request, StreamObserver<NrcCodeListResponse> responseObserver) {
        NrcCodeListResponse response = nrcServiceBlockingStub.getAllNrcCodes(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getNrcValuesByCodeName(GetNrcValuesByCodeNameRequest request, StreamObserver<NrcCodeValueListResponse> responseObserver) {
        NrcCodeValueListResponse response = nrcServiceBlockingStub.getNrcValuesByCodeName(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
