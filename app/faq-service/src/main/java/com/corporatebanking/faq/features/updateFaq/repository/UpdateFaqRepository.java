package com.corporatebanking.faq.features.updateFaq.repository;

import com.corporatebanking.faq.features.updateFaq.model.UpdateFaqModel;

import java.util.Optional;

public interface UpdateFaqRepository {
  public Optional<UpdateFaqModel> findById(int id);
  public UpdateFaqModel update(UpdateFaqModel updateFaqModel);
}
