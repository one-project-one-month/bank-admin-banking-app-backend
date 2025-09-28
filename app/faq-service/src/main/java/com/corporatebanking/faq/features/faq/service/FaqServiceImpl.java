package com.corporatebanking.faq.features.faq.service;

import com.corporatebanking.faq.features.faq.model.FaqData;
import com.corporatebanking.faq.features.faq.repository.jdbc.FaqJdbcRepository;
import com.corporatebanking.faq.grpc.FaqRequest;
import com.corporatebanking.faq.grpc.FaqResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class FaqServiceImpl extends com.corporatebanking.faq.grpc.FaqServiceGrpc.FaqServiceImplBase {

    private final FaqJdbcRepository faqJdbcRepository;

    @Autowired
    public FaqServiceImpl(FaqJdbcRepository faqJdbcRepository){
        this.faqJdbcRepository = faqJdbcRepository;
    }

    @Override
    public void createFaq(FaqRequest request, StreamObserver<FaqResponse> responseObserver) {
        FaqData faqData = new FaqData(
                null,
                request.getQuestion(),
                request.getAnswer(),
                null,
                null
        );

        FaqData result = faqJdbcRepository.save(faqData);
        FaqResponse faqResponse = FaqResponse.newBuilder()
                .setId(result.id())
                .setQuestion(result.question())
                .setAnswer(result.answer())
                .setCreatedAt(result.createdAt().toString())
                .build();

        responseObserver.onNext(faqResponse);
        responseObserver.onCompleted();
    }
}
