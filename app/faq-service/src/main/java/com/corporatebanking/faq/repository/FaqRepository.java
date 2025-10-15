package com.corporatebanking.faq.repository;

import com.corporatebanking.faq.model.Faq;
import com.corporatebanking.faq.model.FaqCategory;

import java.util.Optional;

public interface FaqRepository {
    Faq save(Faq faq);
    Optional<Faq> findById(Integer id);
    Optional<Faq> update(Faq faq);
    void deleteById(Integer id);
    FaqCategory saveCategory(FaqCategory faqCategory);
    Optional<FaqCategory> findCategoryById(Integer id);
}
