package com.corporatebanking.gateway.controller.faq;

import com.corporatebanking.faq.grpc.*;
import com.corporatebanking.gateway.dto.faq.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<CreateFaqResponseDto<CreateFaqGrpcResponseDto>> createFaq(@RequestBody CreateFaqRequestDto createFaqRequestDto){

        try{

            CreateFaqRequest createFaqRequest = CreateFaqRequest.newBuilder()
                    .setQuestion(createFaqRequestDto.question())
                    .setAnswer(createFaqRequestDto.answer())
                    .setCategoryId(createFaqRequestDto.categoryId())
                    .build();

            System.out.println("CreateFaqRequest : " + createFaqRequestDto.categoryId());
            CreateFaqResponse createFaqResponse = createFaqServiceBlockingStub.createFaq(createFaqRequest);
            CreateFaqResponseDto<CreateFaqGrpcResponseDto> createFaqResponseDto = new CreateFaqResponseDto<>(
                    HttpStatus.OK.value(),
                    "FAQ is created successfully",
                    toDto(createFaqResponse)
            );
            return ResponseEntity.ok(createFaqResponseDto);

        } catch (io.grpc.StatusRuntimeException e){
            HttpStatus status = mapGrpcStatusToHttp(e.getStatus().getCode());

            CreateFaqResponseDto<CreateFaqGrpcResponseDto> createFaqResponseDto = new CreateFaqResponseDto<>(
                    status.value(),
                    e.getStatus().getDescription() == null ? "UnExpected Error" : e.getStatus().getDescription(),
                    null
            );
            return ResponseEntity.status(status).body(createFaqResponseDto);
        }
    }

    @PostMapping("faq-category")
    public ResponseEntity<CreateFaqResponseDto<CreateFaqCategoryResponseDto>> createFaqCategory(@RequestBody CreateFaqCategoryRequestDto faqCategoryRequestDto){
        try{
            CreateFaqCategoryRequest faqCategoryRequest = CreateFaqCategoryRequest.newBuilder()
                    .setName(faqCategoryRequestDto.name())
                    .build();

            CreateFaqCategoryResponse faqCategoryResponse = createFaqServiceBlockingStub.createFaqCategory(faqCategoryRequest);

            CreateFaqResponseDto<CreateFaqCategoryResponseDto> createFaqResponseDto = new CreateFaqResponseDto<>(
                    HttpStatus.OK.value(),
                    "FAQ is created successfully",
                    new CreateFaqCategoryResponseDto(faqCategoryResponse.getId(),faqCategoryResponse.getName())
            );

            return ResponseEntity.ok(createFaqResponseDto);

        } catch (io.grpc.StatusRuntimeException e){
            HttpStatus status = mapGrpcStatusToHttp(e.getStatus().getCode());

            CreateFaqResponseDto<CreateFaqCategoryResponseDto> createFaqResponseDto = new CreateFaqResponseDto<>(
                    status.value(),
                    e.getStatus().getDescription() == null ? "UnExpected Error" : e.getStatus().getDescription(),
                    null
            );
            return ResponseEntity.status(status).body(createFaqResponseDto);
        }
    }

    private CreateFaqGrpcResponseDto toDto(CreateFaqResponse faqResponse){

        CreateFaqCategoryResponseDto categoryResponseDto = new CreateFaqCategoryResponseDto(
                faqResponse.getCreateFaqCategoryResponse().getId(),
                faqResponse.getCreateFaqCategoryResponse().getName()
        );

        return new CreateFaqGrpcResponseDto(
                faqResponse.getId(),
                faqResponse.getQuestion(),
                faqResponse.getAnswer(),
                categoryResponseDto,
                faqResponse.getCreatedAt(),
                faqResponse.getUpdatedAt()
        );
    }

    private HttpStatus mapGrpcStatusToHttp(io.grpc.Status.Code code){
        return switch (code){
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_ARGUMENT -> HttpStatus.BAD_REQUEST;
            case FAILED_PRECONDITION -> HttpStatus.CONFLICT;
            case UNAVAILABLE -> HttpStatus.SERVICE_UNAVAILABLE;
            case INTERNAL -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
