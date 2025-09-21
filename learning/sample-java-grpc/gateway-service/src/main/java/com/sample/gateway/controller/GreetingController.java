package com.sample.gateway.controller;

import com.sample.greeting.grpc.GreetingRequest;
import com.sample.greeting.grpc.GreetingServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GrpcClient("greeting-service")
    private GreetingServiceGrpc.GreetingServiceBlockingStub greetingServiceBlockingStub;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", defaultValue = "World") String name) {
        GreetingRequest greetingRequest = GreetingRequest.newBuilder()
                .setName(name)
                .build();
        return greetingServiceBlockingStub.greeting(greetingRequest).getMessage();
    }
}
