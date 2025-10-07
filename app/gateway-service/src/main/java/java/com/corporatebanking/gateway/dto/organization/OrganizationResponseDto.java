package java.com.corporatebanking.gateway.dto.organization;

public record OrganizationResponseDto(
    Long id,
    String name,
    String shortcode,
    String address,
    String country,
    String createdAt,
    String updatedAt,
    Long createdBy,
    Long updatedBy
) {}
