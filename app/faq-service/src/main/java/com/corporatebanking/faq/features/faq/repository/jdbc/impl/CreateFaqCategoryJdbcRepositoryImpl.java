package com.corporatebanking.faq.features.faq.repository.jdbc.impl;

import com.corporatebanking.faq.features.faq.exceptions.FaqNotFoundAfterInsertException;
import com.corporatebanking.faq.features.faq.model.CreateFaqCategoryData;
import com.corporatebanking.faq.features.faq.repository.jdbc.CreateFaqCategoryJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class CreateFaqCategoryJdbcRepositoryImpl implements CreateFaqCategoryJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public CreateFaqCategoryJdbcRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CreateFaqCategoryData> faqCategoryDataRowMapper = (rs, rowNum) -> new CreateFaqCategoryData(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getDate("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime(): null,
            rs.getDate("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
    );

    @Override
    public CreateFaqCategoryData save(CreateFaqCategoryData createFaqCategoryData) {

        String sql = "INSERT INTO FAQ_Category (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, createFaqCategoryData.name());

            return ps;
        }, keyHolder);

        int generatedId = (int) keyHolder.getKeys().get("id");
        return findById(generatedId).orElseThrow(() ->
                new FaqNotFoundAfterInsertException("FAQ category is not found after inserting id=" + generatedId)
        );
    }

    @Override
    public Optional<CreateFaqCategoryData> findById(int id) {
        String sql = "SELECT * FROM faq_category WHERE id=? ";
        return jdbcTemplate.query(sql, faqCategoryDataRowMapper, id).stream().findFirst();
    }
}
