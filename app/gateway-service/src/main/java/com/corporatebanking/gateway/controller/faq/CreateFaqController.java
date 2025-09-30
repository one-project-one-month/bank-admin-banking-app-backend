package com.corporatebanking.gateway.controller.faq;

import com.corporatebanking.faq.grpc.CreateFaqRequest;
import com.corporatebanking.faq.grpc.CreateFaqResponse;
import com.corporatebanking.faq.grpc.CreateFaqServiceGrpc;
import com.corporatebanking.gateway.dto.faq.CreateFaqRequestDto;
import com.corporatebanking.gateway.dto.faq.CreateFaqResponseDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;

@RestController
@RequestMapping("/api/v1/faq")
public class CreateFaqController {

    @GrpcClient("faq-service")
    private CreateFaqServiceGrpc.CreateFaqServiceBlockingStub createFaqServiceBlockingStub;

    @PostMapping
    public ResponseEntity<CreateFaqResponseDto> createFaq(@RequestBody CreateFaqRequestDto createFaqRequestDto){

        CreateFaqRequest createFaqRequest = CreateFaqRequest.newBuilder()
                .setQuestion(createFaqRequestDto.question())
                .setAnswer(createFaqRequestDto.answer())
                .build();

        CreateFaqResponse createFaqResponse = createFaqServiceBlockingStub.createFaq(createFaqRequest);
        return ResponseEntity.ok(toDto(createFaqResponse));
    }

    private CreateFaqResponseDto toDto(CreateFaqResponse faqResponse){

        return new CreateFaqResponseDto(
                faqResponse.getId(),
                faqResponse.getQuestion(),
                faqResponse.getAnswer(),
                faqResponse.getCreatedAt(),
                faqResponse.getUpdatedAt()
        );
    }
}
