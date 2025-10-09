package com.corporatebanking.faq.features.faq.repository.jdbc;

import com.corporatebanking.faq.features.faq.model.CreateFaqCategoryData;

import java.util.Optional;

public interface CreateFaqCategoryJdbcRepository {
    public CreateFaqCategoryData save(CreateFaqCategoryData createFaqCategoryData);
    public Optional<CreateFaqCategoryData> findById(int id);
}
