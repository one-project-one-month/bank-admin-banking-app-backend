package com.corporatebanking.faq.features.faq.repository.jdbc;

import com.corporatebanking.faq.features.faq.model.FaqData;

import java.util.Optional;

public interface FaqJdbcRepository {
    public FaqData save(FaqData faqData);
    public Optional<FaqData> findById(Integer id);
}
