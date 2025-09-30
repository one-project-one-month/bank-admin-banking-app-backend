package com.corporatebanking.faq.features.faq.repository.jdbc;

import com.corporatebanking.faq.features.faq.model.CreateFaqData;

import java.util.Optional;

public interface CreateFaqJdbcRepository {
    public CreateFaqData save(CreateFaqData createFaqData);
    public Optional<CreateFaqData> findById(Integer id);
}
