package com.corporatebanking.gateway.dto.faq;

public record UpdateFaqGrpcResponseDto(
  String question,
  String answer,
  Long createdAt,
  Long updatedAt
) { }
