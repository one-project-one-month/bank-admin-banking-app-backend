package com.corporatebanking.faq.features.faq.service;

import com.corporatebanking.faq.features.faq.exceptions.CategoryNotFoundException;
import com.corporatebanking.faq.features.faq.exceptions.FaqNotFoundAfterInsertException;
import com.corporatebanking.faq.features.faq.model.CreateFaqCategoryData;
import com.corporatebanking.faq.features.faq.model.CreateFaqData;
import com.corporatebanking.faq.features.faq.repository.jdbc.CreateFaqCategoryJdbcRepository;
import com.corporatebanking.faq.features.faq.repository.jdbc.CreateFaqJdbcRepository;
import com.corporatebanking.faq.grpc.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Optional;

@GrpcService
public class CreateFaqServiceImpl extends CreateFaqServiceGrpc.CreateFaqServiceImplBase {

    private final CreateFaqJdbcRepository faqJdbcRepository;
    private final CreateFaqCategoryJdbcRepository faqCategoryJdbcRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public CreateFaqServiceImpl(CreateFaqJdbcRepository faqJdbcRepository, CreateFaqCategoryJdbcRepository faqCategoryJdbcRepository){
        this.faqJdbcRepository = faqJdbcRepository;
        this.faqCategoryJdbcRepository = faqCategoryJdbcRepository;
    }

    @Override
    public void createFaq(CreateFaqRequest request, StreamObserver<CreateFaqResponse> responseObserver) {

        try{
            System.out.println("Getting data from GateWay, category ID : " + request.getCategoryId());
            CreateFaqCategoryData faqCategoryData = faqCategoryJdbcRepository.findById((int) request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category Id is not found"));

            CreateFaqData faqData = new CreateFaqData(
                    null,
                    request.getQuestion(),
                    request.getAnswer(),
                    request.getCategoryId(),
                    null,
                    null
            );

            CreateFaqData result = faqJdbcRepository.save(faqData);

            CreateFaqCategoryResponse createFaqCategoryResponseResponse = CreateFaqCategoryResponse.newBuilder()
                    .setId(faqCategoryData.id())
                    .setName(faqCategoryData.name())
                    .build();

            CreateFaqResponse faqResponse = CreateFaqResponse.newBuilder()
                    .setId(result.id())
                    .setQuestion(result.question())
                    .setAnswer(result.answer())
                    .setCreateFaqCategoryResponse(createFaqCategoryResponseResponse)
                    .setCreatedAt(result.createdAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                    .setUpdatedAt(result.updatedAt() == null ? 0 : result.updatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                    .build();

            responseObserver.onNext(faqResponse);
            responseObserver.onCompleted();

        } catch (CategoryNotFoundException ex) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(ex.getMessage())
                            .asRuntimeException()
            );
        } catch (FaqNotFoundAfterInsertException ex){
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription(ex.getMessage())
                            .asRuntimeException()
            );
        }
        catch (Exception ex) {
            responseObserver.onError(
                    Status.UNKNOWN
                            .withDescription(ex.getMessage())
                            .asRuntimeException()
            );
        }
    }
    public void createFaqCategory(CreateFaqCategoryRequest request, StreamObserver<CreateFaqCategoryResponse> responseObserver) {

        try{

            CreateFaqCategoryData faqCategoryData = new CreateFaqCategoryData(
                    null,
                    request.getName(),
                    null,null
            );

            CreateFaqCategoryData result = faqCategoryJdbcRepository.save(faqCategoryData);
            CreateFaqCategoryResponse categoryResponse = CreateFaqCategoryResponse.newBuilder()
                    .setId(result.id())
                    .setName(result.name())
                    .build();

            responseObserver.onNext(categoryResponse);
            responseObserver.onCompleted();

        } catch (FaqNotFoundAfterInsertException ex){
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription(ex.getMessage())
                            .asRuntimeException()
            );
        }
        catch (Exception ex) {
            responseObserver.onError(
                    Status.UNKNOWN
                            .withDescription(ex.getMessage())
                            .asRuntimeException()
            );
        }
    }

}
