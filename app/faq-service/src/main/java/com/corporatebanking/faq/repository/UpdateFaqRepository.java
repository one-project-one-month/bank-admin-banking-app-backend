package com.corporatebanking.faq.repository;

import com.corporatebanking.faq.model.UpdateFaqModel;

import java.util.Optional;

public interface UpdateFaqRepository {
  public Optional<UpdateFaqModel> findById(int id);
  public UpdateFaqModel update(UpdateFaqModel updateFaqModel);
}
