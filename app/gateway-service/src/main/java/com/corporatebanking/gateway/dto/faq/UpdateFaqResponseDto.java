package com.corporatebanking.gateway.dto.faq;

public record UpdateFaqResponseDto<T> (
  int code,
  String message,
  T data
) { }
