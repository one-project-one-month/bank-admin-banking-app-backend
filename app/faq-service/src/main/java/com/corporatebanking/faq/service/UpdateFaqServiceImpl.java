package com.corporatebanking.faq.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

import com.corporatebanking.faq.grpc.UpdateFaqRequest;
import com.corporatebanking.faq.grpc.UpdateFaqResponse;
import com.corporatebanking.faq.grpc.UpdateFaqCategoryResponse;
import com.corporatebanking.faq.grpc.UpdateFaqServiceGrpc;

import com.corporatebanking.faq.repository.UpdateFaqRepository;
import com.corporatebanking.faq.repository.UpdateFaqCategoryRepository;

import com.corporatebanking.faq.model.UpdateFaqModel;
import com.corporatebanking.faq.model.UpdateFaqCategoryModel;

@GrpcService
public class UpdateFaqServiceImpl extends UpdateFaqServiceGrpc.UpdateFaqServiceImplBase {
  
  private final UpdateFaqRepository updateFaqRepository;
  private final UpdateFaqCategoryRepository updateFaqCategoryRepository;

  @Autowired
  public UpdateFaqServiceImpl(UpdateFaqRepository updateFaqRepository, UpdateFaqCategoryRepository updateFaqCategoryRepository) {
    this.updateFaqRepository = updateFaqRepository;
    this.updateFaqCategoryRepository = updateFaqCategoryRepository;
  }
    
  @Override
  public void updateFaq(UpdateFaqRequest request, StreamObserver<UpdateFaqResponse> responseObserver) {
    try {
      int id = request.getId();
      String updatedQuestion = request.getQuestion();
      String updatedAnswer = request.getAnswer();
      int categoryId = request.getCategoryId();
      
      UpdateFaqModel existingFaq = updateFaqRepository.findById(id).orElseThrow(() -> new IllegalStateException("Faq not found with id="+id));
      UpdateFaqCategoryModel faqCategory = updateFaqCategoryRepository.findById(categoryId).orElseThrow(() -> new IllegalStateException("Faq not found with id="+id));

      UpdateFaqModel updateFaq = new UpdateFaqModel(
        existingFaq.id(),
        updatedQuestion,
        updatedAnswer,
        categoryId,
        existingFaq.updatedAt(),
        LocalDateTime.now()
      );

      UpdateFaqModel dbResponse = updateFaqRepository.update(updateFaq);

      // Set Respond message
      UpdateFaqCategoryResponse categoryResponse = UpdateFaqCategoryResponse.newBuilder()
        .setId(dbResponse.categoryId())
        .setName(faqCategory.name())
        .build();

      UpdateFaqResponse grpcResponse = UpdateFaqResponse.newBuilder()
        .setId(dbResponse.id())
        .setQuestion(dbResponse.question())
        .setAnswer(dbResponse.answer())
        .setUpdateFaqCategoryResponse(categoryResponse)
        .setCreatedAt(dbResponse.createdAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        .setUpdatedAt(dbResponse.updatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        .build();

      // Send the response
      responseObserver.onNext(grpcResponse);
      responseObserver.onCompleted();
    } catch (Exception e) {
      throw new IllegalStateException("Faq error: " + e.getMessage());
    }
  }
}
