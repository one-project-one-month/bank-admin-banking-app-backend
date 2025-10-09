package com.corporatebanking.faq.repository;

import com.corporatebanking.faq.model.UpdateFaqCategoryModel;

import java.util.Optional;

public interface UpdateFaqCategoryRepository {

  public Optional<UpdateFaqCategoryModel> findById(int id);
   
}
