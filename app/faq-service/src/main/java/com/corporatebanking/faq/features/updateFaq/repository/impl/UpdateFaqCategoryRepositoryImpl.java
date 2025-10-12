package com.corporatebanking.faq.features.updateFaq.repository.impl;

import com.corporatebanking.faq.features.updateFaq.model.UpdateFaqCategoryModel;
import com.corporatebanking.faq.features.updateFaq.repository.UpdateFaqCategoryRepository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.util.Optional;

import javax.sql.RowSetEvent;

import java.util.List;

@Repository
public class UpdateFaqCategoryRepositoryImpl implements UpdateFaqCategoryRepository{

  private final JdbcTemplate jdbcTemplate;

  public UpdateFaqCategoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<UpdateFaqCategoryModel> updateFaqCategoryRowMapper = (rs, rowNum) -> new UpdateFaqCategoryModel(
    rs.getInt("id"),
    rs.getString("name"),
    rs.getDate("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
    rs.getDate("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
  );

  @Override
  public Optional<UpdateFaqCategoryModel> findById(int id) {
    String sql = "SELECT * FROM faq_category WHERE id = ?";
    List<UpdateFaqCategoryModel> faqCategoryList = jdbcTemplate.query(sql, updateFaqCategoryRowMapper, id);
      return faqCategoryList.stream().findFirst();
  }
  
}
