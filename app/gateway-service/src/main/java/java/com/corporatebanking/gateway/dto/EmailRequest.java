package java.com.corporatebanking.gateway.dto;

public record EmailRequest(
    String to,
    String from,
    String subject,
    String body
) {}
