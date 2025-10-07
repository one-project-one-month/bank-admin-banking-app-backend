package com.corporatebanking.faq.model;

import java.time.LocalDateTime;

public record UpdateFaqModel (
  Integer id,
  String question,
  String answer,
  Integer categoryId,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) { }
