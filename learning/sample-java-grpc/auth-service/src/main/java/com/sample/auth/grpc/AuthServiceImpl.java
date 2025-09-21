package com.sample.auth.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.kafka.core.KafkaTemplate;

@GrpcService
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public AuthServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        String message = "Hello from login " + request.getUsername() + ", " + request.getPassword();
        kafkaTemplate.send("auth-topic", "login", message);
        LoginResponse response = LoginResponse.newBuilder()
                .setMessage(message)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
        String message = "Hello from register " + request.getUsername() + ", " + request.getPassword();
        kafkaTemplate.send("auth-topic", "register", message);
        RegisterResponse response = RegisterResponse.newBuilder()
                .setMessage(message)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
