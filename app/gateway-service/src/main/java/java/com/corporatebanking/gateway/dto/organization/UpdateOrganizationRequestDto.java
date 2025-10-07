package java.com.corporatebanking.gateway.dto.organization;

public record UpdateOrganizationRequestDto(
    String name,
    String shortcode,
    String address,
    String country,
    Long updatedBy
) {}
