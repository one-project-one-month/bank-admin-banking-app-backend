package com.corporatebanking.gateway.controller.users;

import com.corporatebanking.organization.grpc.*;
import com.corporatebanking.gateway.dto.organization.CreateOrganizationRequestDto;
import com.corporatebanking.gateway.dto.organization.OrganizationResponseDto;
import com.corporatebanking.gateway.dto.organization.UpdateOrganizationRequestDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    @GrpcClient("user-service")
    private OrganizationServiceGrpc.OrganizationServiceBlockingStub organizationServiceStub;

    @PostMapping
    public ResponseEntity<OrganizationResponseDto> createOrganization(@RequestBody CreateOrganizationRequestDto requestDto) {
        CreateOrganizationRequest request = CreateOrganizationRequest.newBuilder()
                .setName(requestDto.name())
                .setShortcode(requestDto.shortcode())
                .setAddress(requestDto.address())
                .setCountry(requestDto.country())
                .setCreatedBy(requestDto.createdBy())
                .build();
        OrganizationResponse response = organizationServiceStub.createOrganization(request);
        return ResponseEntity.ok(toDto(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponseDto> getOrganization(@PathVariable Long id) {
        GetOrganizationRequest request = GetOrganizationRequest.newBuilder().setId(id).build();
        OrganizationResponse response = organizationServiceStub.getOrganization(request);
        return ResponseEntity.ok(toDto(response));
    }

    @GetMapping
    public ResponseEntity<List<OrganizationResponseDto>> getAllOrganizations() {
        GetAllOrganizationsRequest request = GetAllOrganizationsRequest.newBuilder().build();
        OrganizationListResponse response = organizationServiceStub.getAllOrganizations(request);
        List<OrganizationResponseDto> dtos = response.getOrganizationsList().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponseDto> updateOrganization(@PathVariable Long id, @RequestBody UpdateOrganizationRequestDto requestDto) {
        UpdateOrganizationRequest request = UpdateOrganizationRequest.newBuilder()
                .setId(id)
                .setName(requestDto.name())
                .setShortcode(requestDto.shortcode())
                .setAddress(requestDto.address())
                .setCountry(requestDto.country())
                .setUpdatedBy(requestDto.updatedBy())
                .build();
        OrganizationResponse response = organizationServiceStub.updateOrganization(request);
        return ResponseEntity.ok(toDto(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrganization(@PathVariable Long id) {
        DeleteOrganizationRequest request = DeleteOrganizationRequest.newBuilder().setId(id).build();
        DeleteOrganizationResponse response = organizationServiceStub.deleteOrganization(request);
        return ResponseEntity.ok(response.getMessage());
    }

    private OrganizationResponseDto toDto(OrganizationResponse response) {
        return new OrganizationResponseDto(
                response.getId(),
                response.getName(),
                response.getShortcode(),
                response.getAddress(),
                response.getCountry(),
                response.getCreatedAt(),
                response.getUpdatedAt(),
                response.getCreatedBy(),
                response.getUpdatedBy()
        );
    }
}
