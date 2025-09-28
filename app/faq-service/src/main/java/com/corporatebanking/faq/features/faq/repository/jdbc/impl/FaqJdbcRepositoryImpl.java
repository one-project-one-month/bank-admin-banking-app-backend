package com.corporatebanking.faq.features.faq.repository.jdbc.impl;

import com.corporatebanking.faq.features.faq.model.FaqData;
import com.corporatebanking.faq.features.faq.repository.jdbc.FaqJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class FaqJdbcRepositoryImpl implements FaqJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public FaqJdbcRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<FaqData> faqDataRowMapper = (rs, rowNum) -> new FaqData(
            rs.getInt("id"),
            rs.getString("question"),
            rs.getString("answer"),
            rs.getDate("created_at") != null ? rs.getDate("created_at").toLocalDate() : null,
            rs.getDate("updated_at") != null ? rs.getDate("updated_at").toLocalDate() : null
    );

    @Override
    public FaqData save(FaqData faqData) {

        String sql = "INSERT INTO FAQ (question,answer) VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,faqData.question());
            ps.setString(2, faqData.answer());
            return ps;
        }, keyHolder);

        int generatedId = (int) keyHolder.getKeys().get("id");
        return findById(generatedId).orElse(null);
    }

    @Override
    public Optional<FaqData> findById(Integer id) {
        String sql = "SELECT * FROM FAQ WHERE id = ? ";
        return jdbcTemplate.query(sql, faqDataRowMapper, id).stream().findFirst();
    }
}
