package com.corporatebanking.gateway.dto.faq;

public record UpdateFaqGrpcResponseDto(
  int id,
  String question,
  String answer,
  UpdateFaqCategoryGrpcResponseDto updateFaqCategoryGrpcResponseDto,
  String createdAt,
  String updatedAt
) { }
