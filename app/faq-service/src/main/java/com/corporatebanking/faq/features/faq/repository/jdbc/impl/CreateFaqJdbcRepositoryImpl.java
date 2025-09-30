package com.corporatebanking.faq.features.faq.repository.jdbc.impl;

import com.corporatebanking.faq.features.faq.model.CreateFaqData;
import com.corporatebanking.faq.features.faq.repository.jdbc.CreateFaqJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class CreateFaqJdbcRepositoryImpl implements CreateFaqJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public CreateFaqJdbcRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CreateFaqData> faqDataRowMapper = (rs, rowNum) -> new CreateFaqData(
            rs.getInt("id"),
            rs.getString("question"),
            rs.getString("answer"),
            rs.getDate("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime(): null,
            rs.getDate("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
    );

    @Override
    public CreateFaqData save(CreateFaqData createFaqData) {

        String sql = "INSERT INTO FAQ (question,answer) VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, createFaqData.question());
            ps.setString(2, createFaqData.answer());
            System.out.println("before query : " + ps);
            return ps;
        }, keyHolder);

        int generatedId = (int) keyHolder.getKeys().get("id");
        return findById(generatedId).orElse(null);
    }

    @Override
    public Optional<CreateFaqData> findById(Integer id) {
        String sql = "SELECT * FROM FAQ WHERE id = ? ";
        return jdbcTemplate.query(sql, faqDataRowMapper, id).stream().findFirst();
    }
}
