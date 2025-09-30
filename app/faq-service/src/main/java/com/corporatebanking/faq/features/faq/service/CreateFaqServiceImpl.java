package com.corporatebanking.faq.features.faq.service;

import com.corporatebanking.faq.features.faq.model.CreateFaqData;
import com.corporatebanking.faq.features.faq.repository.jdbc.CreateFaqJdbcRepository;
import com.corporatebanking.faq.grpc.CreateFaqRequest;
import com.corporatebanking.faq.grpc.CreateFaqResponse;
import com.corporatebanking.faq.grpc.CreateFaqServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@GrpcService
public class CreateFaqServiceImpl extends CreateFaqServiceGrpc.CreateFaqServiceImplBase {

    private final CreateFaqJdbcRepository faqJdbcRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public CreateFaqServiceImpl(CreateFaqJdbcRepository faqJdbcRepository){
        this.faqJdbcRepository = faqJdbcRepository;
    }

    @Override
    public void createFaq(CreateFaqRequest request, StreamObserver<CreateFaqResponse> responseObserver) {
        CreateFaqData faqData = new CreateFaqData(
                null,
                request.getQuestion(),
                request.getAnswer(),
                null,
                null
        );

        CreateFaqData result = faqJdbcRepository.save(faqData);
        CreateFaqResponse faqResponse = CreateFaqResponse.newBuilder()
                .setId(result.id())
                .setQuestion(result.question())
                .setAnswer(result.answer())
                .setCreatedAt(result.createdAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .build();

        responseObserver.onNext(faqResponse);
        responseObserver.onCompleted();
    }
}
