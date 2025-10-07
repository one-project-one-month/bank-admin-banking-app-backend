package com.corporatebanking.user.features.organizations.service;

import com.corporatebanking.organization.grpc.*;
import com.corporatebanking.user.features.organizations.models.OrganizationData;
import com.corporatebanking.user.features.organizations.repository.jdbc.OrganizationJdbcRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GrpcService
public class OrganizationServiceImpl extends OrganizationServiceGrpc.OrganizationServiceImplBase {

    private final OrganizationJdbcRepository organizationRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Autowired
    public OrganizationServiceImpl(OrganizationJdbcRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public void createOrganization(CreateOrganizationRequest request, StreamObserver<OrganizationResponse> responseObserver) {
        OrganizationData organization = new OrganizationData(
                null,
                request.getName(),
                request.getShortcode(),
                request.getAddress(),
                request.getCountry(),
                null, null,
                request.getCreatedBy(),
                null
        );

        OrganizationData savedOrganization = organizationRepository.save(organization);
        responseObserver.onNext(toOrganizationResponse(savedOrganization));
        responseObserver.onCompleted();
    }

    @Override
    public void getOrganization(GetOrganizationRequest request, StreamObserver<OrganizationResponse> responseObserver) {
        organizationRepository.findById(request.getId()).ifPresent(organization -> {
            responseObserver.onNext(toOrganizationResponse(organization));
        });
        responseObserver.onCompleted();
    }

    @Override
    public void getAllOrganizations(GetAllOrganizationsRequest request, StreamObserver<OrganizationListResponse> responseObserver) {
        List<OrganizationData> organizations = organizationRepository.findAll();
        List<OrganizationResponse> organizationResponses = organizations.stream()
                .map(this::toOrganizationResponse)
                .collect(Collectors.toList());

        OrganizationListResponse response = OrganizationListResponse.newBuilder()
                .addAllOrganizations(organizationResponses)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateOrganization(UpdateOrganizationRequest request, StreamObserver<OrganizationResponse> responseObserver) {
        Optional<OrganizationData> existingOrgOpt = organizationRepository.findById(request.getId());
        if (existingOrgOpt.isPresent()) {
             OrganizationData orgToUpdate = new OrganizationData(
                    request.getId(),
                    request.getName(),
                    request.getShortcode(),
                    request.getAddress(),
                    request.getCountry(),
                    existingOrgOpt.get().createdAt(),
                    null,
                    existingOrgOpt.get().createdBy(),
                    request.getUpdatedBy()
             );
            organizationRepository.update(orgToUpdate).ifPresent(updatedOrg -> {
                 responseObserver.onNext(toOrganizationResponse(updatedOrg));
            });
        }
        responseObserver.onCompleted();
    }

    @Override
    public void deleteOrganization(DeleteOrganizationRequest request, StreamObserver<DeleteOrganizationResponse> responseObserver) {
        organizationRepository.deleteById(request.getId());
        DeleteOrganizationResponse response = DeleteOrganizationResponse.newBuilder()
                .setMessage("OrganizationData with ID " + request.getId() + " deleted successfully.")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private OrganizationResponse toOrganizationResponse(OrganizationData organization) {
        OrganizationResponse.Builder builder = OrganizationResponse.newBuilder();
        builder.setId(organization.id())
                .setName(organization.name())
                .setShortcode(organization.shortCode())
                .setAddress(organization.address())
                .setCountry(organization.country());

        if (organization.createdAt() != null) {
            builder.setCreatedAt(organization.createdAt().format(dateFormatter));
        }
        if (organization.updatedAt() != null) {
            builder.setUpdatedAt(organization.updatedAt().format(dateFormatter));
        }
        if (organization.createdBy() != null) {
            builder.setCreatedBy(organization.createdBy());
        }
        if (organization.updatedBy() != null) {
            builder.setUpdatedBy(organization.updatedBy());
        }

        return builder.build();
    }
}
