package com.corporatebanking.faq.features.updateFaq.repository.impl;

import com.corporatebanking.faq.features.updateFaq.model.UpdateFaqModel;
import com.corporatebanking.faq.features.updateFaq.repository.UpdateFaqRepository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.util.Optional;
import java.util.List;

@Repository
public class UpdateFaqRepositoryImpl implements UpdateFaqRepository {
  
  private final JdbcTemplate jdbcTemplate;

  public UpdateFaqRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<UpdateFaqModel> updateFaqRowMapper = (rs, rowNum) -> new UpdateFaqModel(
    rs.getInt("id"),
    rs.getString("question"),
    rs.getString("answer"),
    rs.getInt("faq_category_id"),
    rs.getDate("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
    rs.getDate("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
  );
 
  @Override
  public Optional<UpdateFaqModel> findById(int id) {
    String sql = "SELECT * FROM faq WHERE id = ?";
    List<UpdateFaqModel> faqList = jdbcTemplate.query(sql,updateFaqRowMapper, id);
      return faqList.stream().findFirst();
  }
 
  @Override
  public UpdateFaqModel update(UpdateFaqModel updateFaqModel) {
    if (updateFaqModel.id() == null)  throw new IllegalArgumentException("Id is required for update");
    String sql = "UPDATE faq SET question = ?, answer = ?, faq_category_id =?, updated_at = ?  WHERE id = ?";

    int effectRow = jdbcTemplate.update(sql,
      updateFaqModel.question(),
      updateFaqModel.answer(),
      updateFaqModel.categoryId(),
      updateFaqModel.updatedAt(),
      updateFaqModel.id());

    if (effectRow == 0) throw new IllegalStateException("FAQ not found with id="+updateFaqModel.id());
    return this.findById(updateFaqModel.id()).orElseThrow();
  }
}
