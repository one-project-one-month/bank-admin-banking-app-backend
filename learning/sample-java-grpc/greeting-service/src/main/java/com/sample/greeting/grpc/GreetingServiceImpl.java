package com.sample.greeting.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.kafka.core.KafkaTemplate;

@GrpcService
public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public GreetingServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void greeting(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        kafkaTemplate.send("greeting-topic", "greeting", "Hello from greeting-service");
        GreetingResponse response = GreetingResponse.newBuilder()
                .setMessage("Hello from greeting-service")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
