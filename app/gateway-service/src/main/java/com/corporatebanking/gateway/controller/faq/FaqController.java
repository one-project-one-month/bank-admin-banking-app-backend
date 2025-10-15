package com.corporatebanking.gateway.controller.faq;

import com.corporatebanking.faq.grpc.*;
import com.corporatebanking.gateway.dto.faq.*;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/faqs")
public class FaqController {

    @GrpcClient("faq-service")
    private FaqServiceGrpc.FaqServiceBlockingStub faqServiceBlockingStub;

    @PostMapping
    public ResponseEntity<CreateFaqResponseDto<CreateFaqGrpcResponseDto>> createFaq(@RequestBody CreateFaqRequestDto createFaqRequestDto) {
        try {
            CreateFaqRequest createFaqRequest = CreateFaqRequest.newBuilder()
                    .setQuestion(createFaqRequestDto.question())
                    .setAnswer(createFaqRequestDto.answer())
                    .setCategoryId(createFaqRequestDto.categoryId())
                    .build();

            FaqResponse faqResponse = faqServiceBlockingStub.createFaq(createFaqRequest);
            CreateFaqResponseDto<CreateFaqGrpcResponseDto> createFaqResponseDto = new CreateFaqResponseDto<>(
                    HttpStatus.OK.value(),
                    "FAQ is created successfully",
                    toDto(faqResponse)
            );
            return ResponseEntity.ok(createFaqResponseDto);
        } catch (io.grpc.StatusRuntimeException e) {
            HttpStatus status = mapGrpcStatusToHttp(e.getStatus().getCode());
            CreateFaqResponseDto<CreateFaqGrpcResponseDto> createFaqResponseDto = new CreateFaqResponseDto<>(
                    status.value(),
                    e.getStatus().getDescription() == null ? "Unexpected Error" : e.getStatus().getDescription(),
                    null
            );
            return ResponseEntity.status(status).body(createFaqResponseDto);
        }
    }

    @PostMapping("/categories")
    public ResponseEntity<CreateFaqResponseDto<CreateFaqCategoryResponseDto>> createFaqCategory(@RequestBody CreateFaqCategoryRequestDto faqCategoryRequestDto) {
        try {
            CreateFaqCategoryRequest faqCategoryRequest = CreateFaqCategoryRequest.newBuilder()
                    .setName(faqCategoryRequestDto.name())
                    .build();

            FaqCategoryResponse faqCategoryResponse = faqServiceBlockingStub.createFaqCategory(faqCategoryRequest);
            CreateFaqResponseDto<CreateFaqCategoryResponseDto> createFaqResponseDto = new CreateFaqResponseDto<>(
                    HttpStatus.OK.value(),
                    "FAQ Category is created successfully",
                    new CreateFaqCategoryResponseDto(faqCategoryResponse.getId(), faqCategoryResponse.getName())
            );
            return ResponseEntity.ok(createFaqResponseDto);
        } catch (io.grpc.StatusRuntimeException e) {
            HttpStatus status = mapGrpcStatusToHttp(e.getStatus().getCode());
            CreateFaqResponseDto<CreateFaqCategoryResponseDto> createFaqResponseDto = new CreateFaqResponseDto<>(
                    status.value(),
                    e.getStatus().getDescription() == null ? "Unexpected Error" : e.getStatus().getDescription(),
                    null
            );
            return ResponseEntity.status(status).body(createFaqResponseDto);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateFaqResponseDto<CreateFaqGrpcResponseDto>> getFaqById(@PathVariable("id") int id) {
        try {
            GetFaqRequest grpcRequest = GetFaqRequest.newBuilder().setId(id).build();
            FaqResponse response = faqServiceBlockingStub.getFaq(grpcRequest);
            CreateFaqResponseDto<CreateFaqGrpcResponseDto> responseDto = new CreateFaqResponseDto<>(
                    HttpStatus.OK.value(),
                    "FAQ retrieved successfully",
                    toDto(response)
            );
            return ResponseEntity.ok(responseDto);
        } catch (io.grpc.StatusRuntimeException e) {
            HttpStatus status = mapGrpcStatusToHttp(e.getStatus().getCode());
            CreateFaqResponseDto<CreateFaqGrpcResponseDto> responseDto = new CreateFaqResponseDto<>(
                    status.value(),
                    e.getStatus().getDescription() == null ? "Unexpected Error" : e.getStatus().getDescription(),
                    null
            );
            return ResponseEntity.status(status).body(responseDto);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateFaqResponseDto<UpdateFaqGrpcResponseDto>> updateFaq(@PathVariable("id") int id, @RequestBody UpdateFaqRequestDto requestDto) {
        try {
            UpdateFaqRequest grpcRequest = UpdateFaqRequest.newBuilder()
                    .setId(id)
                    .setQuestion(requestDto.question())
                    .setAnswer(requestDto.answer())
                    .setCategoryId(requestDto.categoryId())
                    .build();

            FaqResponse grpcResponse = faqServiceBlockingStub.updateFaq(grpcRequest);
            UpdateFaqGrpcResponseDto grpcResponseDto = toUpdateDto(grpcResponse);
            UpdateFaqResponseDto<UpdateFaqGrpcResponseDto> response = new UpdateFaqResponseDto<>(
                    HttpStatus.OK.value(),
                    "FAQ is updated successfully",
                    grpcResponseDto
            );
            return ResponseEntity.ok(response);
        } catch (StatusRuntimeException e) {
            HttpStatus status = mapGrpcStatusToHttp(e.getStatus().getCode());
            String message = e.getStatus().getDescription() == null ? "Unexpected Error" : e.getStatus().getDescription();
            UpdateFaqResponseDto<UpdateFaqGrpcResponseDto> response = new UpdateFaqResponseDto<>(
                    status.value(),
                    message,
                    null
            );
            return ResponseEntity.status(status).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CreateFaqResponseDto<Object>> deleteFaqById(@PathVariable("id") int id) {
        try {
            DeleteFaqRequest grpcRequest = DeleteFaqRequest.newBuilder().setId(id).build();
            DeleteFaqResponse response = faqServiceBlockingStub.deleteFaq(grpcRequest);
            CreateFaqResponseDto<Object> responseDto = new CreateFaqResponseDto<>(
                    HttpStatus.OK.value(),
                    response.getMessage(),
                    ""
            );
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (io.grpc.StatusRuntimeException e) {
            HttpStatus status = mapGrpcStatusToHttp(e.getStatus().getCode());
            CreateFaqResponseDto<Object> responseDto = new CreateFaqResponseDto<>(
                    status.value(),
                    e.getStatus().getDescription() == null ? "Unexpected Error" : e.getStatus().getDescription(),
                    null
            );
            return ResponseEntity.status(status).body(responseDto);
        }
    }

    private CreateFaqGrpcResponseDto toDto(FaqResponse faqResponse) {
        CreateFaqCategoryResponseDto categoryResponseDto = new CreateFaqCategoryResponseDto(
                faqResponse.getCategory().getId(),
                faqResponse.getCategory().getName()
        );
        return new CreateFaqGrpcResponseDto(
                faqResponse.getId(),
                faqResponse.getQuestion(),
                faqResponse.getAnswer(),
                categoryResponseDto,
                Long.parseLong(faqResponse.getCreatedAt()),
                Long.parseLong(faqResponse.getUpdatedAt())
        );
    }

    private UpdateFaqGrpcResponseDto toUpdateDto(FaqResponse grpcResponse) {
        UpdateFaqCategoryGrpcResponseDto updateFaqCategoryGrpcResponseDto = new UpdateFaqCategoryGrpcResponseDto(
                grpcResponse.getCategory().getId(),
                grpcResponse.getCategory().getName()
        );
        return new UpdateFaqGrpcResponseDto(
                grpcResponse.getId(),
                grpcResponse.getQuestion(),
                grpcResponse.getAnswer(),
                updateFaqCategoryGrpcResponseDto,
                grpcResponse.getCreatedAt(),
                grpcResponse.getUpdatedAt()
        );
    }

    private HttpStatus mapGrpcStatusToHttp(io.grpc.Status.Code code) {
        return switch (code) {
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_ARGUMENT -> HttpStatus.BAD_REQUEST;
            case FAILED_PRECONDITION -> HttpStatus.CONFLICT;
            case UNAVAILABLE -> HttpStatus.SERVICE_UNAVAILABLE;
            case INTERNAL -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
