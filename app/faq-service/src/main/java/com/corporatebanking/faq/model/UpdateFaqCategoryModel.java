package com.corporatebanking.faq.model;

import java.time.LocalDateTime;

public record UpdateFaqCategoryModel (
  Integer id,
  String name,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) { }
