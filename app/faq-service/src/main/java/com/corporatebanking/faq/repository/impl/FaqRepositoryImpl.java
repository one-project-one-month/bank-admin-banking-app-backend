package com.corporatebanking.faq.repository.impl;

import com.corporatebanking.faq.model.Faq;
import com.corporatebanking.faq.model.FaqCategory;
import com.corporatebanking.faq.repository.FaqRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Repository
public class FaqRepositoryImpl implements FaqRepository {

    private final JdbcTemplate jdbcTemplate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public FaqRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Faq> faqRowMapper = (rs, rowNum) -> new Faq(
            rs.getInt("id"),
            rs.getString("question"),
            rs.getString("answer"),
            rs.getInt("faq_category_id"),
            rs.getString("created_at"),
            rs.getString("updated_at")
    );

    private final RowMapper<FaqCategory> faqCategoryRowMapper = (rs, rowNum) -> new FaqCategory(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("created_at"),
            rs.getString("updated_at")
    );

    @Override
    public Faq save(Faq faq) {
        String sql = "INSERT INTO faq (question, answer, faq_category_id, created_at) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, faq.question());
            ps.setString(2, faq.answer());
            ps.setInt(3, faq.categoryId());
            ps.setString(4, LocalDateTime.now().format(formatter));
            return ps;
        }, keyHolder);

        int generatedId = (int) keyHolder.getKeys().get("id");
        return findById(generatedId).orElse(null);
    }

    @Override
    public Optional<Faq> findById(Integer id) {
        String sql = "SELECT * FROM faq WHERE id = ?";
        return jdbcTemplate.query(sql, faqRowMapper, id).stream().findFirst();
    }

    @Override
    public Optional<Faq> update(Faq faq) {
        String sql = "UPDATE faq SET question = ?, answer = ?, faq_category_id = ?, updated_at = ? WHERE id = ?";
        int updatedRows = jdbcTemplate.update(sql,
                faq.question(),
                faq.answer(),
                faq.categoryId(),
                LocalDateTime.now().format(formatter),
                faq.id());

        if (updatedRows > 0) {
            return findById(faq.id());
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM faq WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public FaqCategory saveCategory(FaqCategory faqCategory) {
        String sql = "INSERT INTO faq_category (name, created_at) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, faqCategory.name());
            ps.setString(2, LocalDateTime.now().format(formatter));
            return ps;
        }, keyHolder);

        int generatedId = (int) keyHolder.getKeys().get("id");
        return findCategoryById(generatedId).orElse(null);
    }

    @Override
    public Optional<FaqCategory> findCategoryById(Integer id) {
        String sql = "SELECT * FROM faq_category WHERE id = ?";
        return jdbcTemplate.query(sql, faqCategoryRowMapper, id).stream().findFirst();
    }
}
