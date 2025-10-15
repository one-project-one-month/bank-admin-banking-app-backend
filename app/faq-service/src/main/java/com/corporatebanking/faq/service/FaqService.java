package com.corporatebanking.faq.service;

import com.corporatebanking.faq.grpc.*;
import com.corporatebanking.faq.model.Faq;
import com.corporatebanking.faq.model.FaqCategory;
import com.corporatebanking.faq.repository.FaqRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
public class FaqService extends FaqServiceGrpc.FaqServiceImplBase {

    private final FaqRepository faqRepository;

    @Autowired
    public FaqService(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    @Override
    public void getFaq(GetFaqRequest request, StreamObserver<FaqResponse> responseObserver) {
        faqRepository.findById(request.getId()).ifPresent(faq -> {
            responseObserver.onNext(toFaqResponse(faq));
        });
        responseObserver.onCompleted();
    }

    @Override
    public void createFaq(CreateFaqRequest request, StreamObserver<FaqResponse> responseObserver) {
        Faq faq = new Faq(
                null,
                request.getQuestion(),
                request.getAnswer(),
                request.getCategoryId(),
                null, null
        );

        Faq savedFaq = faqRepository.save(faq);
        responseObserver.onNext(toFaqResponse(savedFaq));
        responseObserver.onCompleted();
    }

    @Override
    public void updateFaq(UpdateFaqRequest request, StreamObserver<FaqResponse> responseObserver) {
        Optional<Faq> existingFaqOpt = faqRepository.findById(request.getId());
        if (existingFaqOpt.isPresent()) {
            Faq faqToUpdate = new Faq(
                    request.getId(),
                    request.getQuestion(),
                    request.getAnswer(),
                    request.getCategoryId(),
                    existingFaqOpt.get().createdAt(),
                    null
            );
            faqRepository.update(faqToUpdate).ifPresent(updatedFaq -> {
                responseObserver.onNext(toFaqResponse(updatedFaq));
            });
        }
        responseObserver.onCompleted();
    }

    @Override
    public void deleteFaq(DeleteFaqRequest request, StreamObserver<DeleteFaqResponse> responseObserver) {
        faqRepository.deleteById(request.getId());
        DeleteFaqResponse response = DeleteFaqResponse.newBuilder()
                .setMessage("Faq with ID " + request.getId() + " deleted successfully.")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createFaqCategory(CreateFaqCategoryRequest request, StreamObserver<FaqCategoryResponse> responseObserver) {
        FaqCategory faqCategory = new FaqCategory(null, request.getName(), null, null);
        FaqCategory savedFaqCategory = faqRepository.saveCategory(faqCategory);
        responseObserver.onNext(toFaqCategoryResponse(savedFaqCategory));
        responseObserver.onCompleted();
    }

    private FaqResponse toFaqResponse(Faq faq) {
        FaqResponse.Builder builder = FaqResponse.newBuilder();
        builder.setId(faq.id())
                .setQuestion(faq.question())
                .setAnswer(faq.answer());

        if (faq.createdAt() != null) {
            builder.setCreatedAt(faq.createdAt());
        }
        if (faq.updatedAt() != null) {
            builder.setUpdatedAt(faq.updatedAt());
        }

        faqRepository.findCategoryById(faq.categoryId()).ifPresent(category -> {
            builder.setCategory(
                com.corporatebanking.faq.grpc.FaqCategory.newBuilder()
                        .setId(category.id())
                        .setName(category.name())
                        .build()
            );
        });


        return builder.build();
    }

    private FaqCategoryResponse toFaqCategoryResponse(FaqCategory faqCategory) {
        return FaqCategoryResponse.newBuilder()
                .setId(faqCategory.id())
                .setName(faqCategory.name())
                .build();
    }
}
