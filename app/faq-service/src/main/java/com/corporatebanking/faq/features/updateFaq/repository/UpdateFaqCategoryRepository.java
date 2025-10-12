package com.corporatebanking.faq.features.updateFaq.repository;

import com.corporatebanking.faq.features.updateFaq.model.UpdateFaqCategoryModel;

import java.util.Optional;

public interface UpdateFaqCategoryRepository {

  public Optional<UpdateFaqCategoryModel> findById(int id);
   
}
