package com.corporatebanking.gateway.controller.faq;

import com.corporatebanking.faq.grpc.UpdateFaqRequest;
import com.corporatebanking.faq.grpc.UpdateFaqResponse;
import com.corporatebanking.faq.grpc.UpdateFaqCategoryResponse;
import com.corporatebanking.faq.grpc.UpdateFaqServiceGrpc;

import com.corporatebanking.gateway.dto.faq.UpdateFaqRequestDto;
import com.corporatebanking.gateway.dto.faq.UpdateFaqResponseDto;
import com.corporatebanking.gateway.dto.faq.UpdateFaqGrpcResponseDto;
import com.corporatebanking.gateway.dto.faq.UpdateFaqCategoryGrpcResponseDto;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/faq")
public class UpdateFaqController {
  
  @GrpcClient("faq-service")
  private UpdateFaqServiceGrpc.UpdateFaqServiceBlockingStub blockingStub;
  
  @PutMapping
  public ResponseEntity<UpdateFaqResponseDto<UpdateFaqGrpcResponseDto>> updateFaq(@RequestBody UpdateFaqRequestDto requestDto) {
    try {
      UpdateFaqRequest grpcRequest = UpdateFaqRequest.newBuilder()
        .setQuestion(requestDto.question())
        .setAnswer(requestDto.answer())
        .build();

      UpdateFaqResponse grpcResponse = blockingStub.updateFaq(grpcRequest);

      UpdateFaqCategoryGrpcResponseDto updateFaqCategoryGrpcResponseDto = new UpdateFaqCategoryGrpcResponseDto(
        grpcResponse.getUpdateFaqCategoryResponse().getId(),
        grpcResponse.getUpdateFaqCategoryResponse().getName()
      );

      UpdateFaqGrpcResponseDto grpcResponseDto = new UpdateFaqGrpcResponseDto(
        grpcResponse.getId(),
        grpcResponse.getQuestion(),
        grpcResponse.getAnswer(),
        updateFaqCategoryGrpcResponseDto,
        grpcResponse.getCreatedAt(),
        grpcResponse.getUpdatedAt()
      );

      UpdateFaqResponseDto<UpdateFaqGrpcResponseDto> response = new UpdateFaqResponseDto<UpdateFaqGrpcResponseDto>(
        HttpStatus.OK.value(),
        "FAQ is updated successfully",
        grpcResponseDto
      );

      return ResponseEntity.ok(response);

    } catch (io.grpc.StatusRuntimeException e) {

      HttpStatus status = mapGrpcStatusToHttp(e.getStatus().getCode());
      String message = e.getStatus().getDescription() == null ? "Unexcepted Error" : e.getStatus().getDescription();
      UpdateFaqResponseDto<UpdateFaqGrpcResponseDto> response = new UpdateFaqResponseDto<UpdateFaqGrpcResponseDto>(
        status.value(),
        message,
        null
      );

      return ResponseEntity.status(status).body(response);
    }
  }

  private HttpStatus mapGrpcStatusToHttp(Status.Code code) {
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
