package com.corporatebanking.faq.service;

// Grpc Import
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import com.corporatebanking.faq.grpc.UpdateFaqRequest;
import com.corporatebanking.faq.grpc.UpdateFaqResponse;
import com.corporatebanking.faq.grpc.UpdateFaqServiceGrpc;

@GrpcService
public class UpdateFaqServiceImpl extends UpdateFaqServiceGrpc.UpdateFaqServiceImplBase {
  @Override
  public void updateFaq(UpdateFaqRequest request, StreamObserver<UpdateFaqResponse> responseObserver) {
    String question = request.getQuestion();
    String answer = request.getAnswer();

    // Set Respond message
    UpdateFaqResponse response = UpdateFaqResponse.newBuilder()
      .setId(0)
      .setQuestion(question)
      .setAnswer(answer)
      .setCreatedAt(0000)
      .setUpdatedAt(1111).build();

    // Send the respond
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
