package com.corporatebanking.faq.features.updateFaq.model;

import java.time.LocalDateTime;

public record UpdateFaqCategoryModel (
  Integer id,
  String name,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) { }
