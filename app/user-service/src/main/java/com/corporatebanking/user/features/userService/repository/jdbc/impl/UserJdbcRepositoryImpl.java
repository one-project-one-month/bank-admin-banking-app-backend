package com.corporatebanking.user.features.userService.repository.jdbc.impl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.corporatebanking.user.features.userService.models.User;
import com.corporatebanking.user.features.userService.repository.jdbc.UserJdbcRepository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserJdbcRepositoryImpl implements UserJdbcRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public UserJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("\"USER\"")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public User save(User user) {

        String sql = "INSERT INTO `USER` (full_name, date_of_birth, gender_id, email, created_by) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.fullName());
            ps.setDate(2, new java.sql.Date(user.dateOfBirth().getTime()));
            ps.setInt(3, user.genderId());
            ps.setString(4, user.email());
            ps.setObject(5, user.createdBy());
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        Long generatedId = ((Number) keys.get("ID")).longValue();
        LocalDate createdAt = keys.get("CREATED_AT") != null
                ? ((Timestamp) keys.get("CREATED_AT")).toLocalDateTime().toLocalDate()
                : null;
        LocalDate updatedAt = keys.get("UPDATED_AT") != null
                ? ((Timestamp) keys.get("UPDATED_AT")).toLocalDateTime().toLocalDate()
                : null;

        String selectSql = "SELECT * FROM `USER` WHERE id = ?";

        User insertedUser = jdbcTemplate.queryForObject(selectSql, new Object[]{generatedId},
                (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("full_name"),
                        rs.getDate("date_of_birth"),
                        rs.getInt("gender_id"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime().toLocalDate() : null,
                        rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime().toLocalDate() : null,
                        rs.getObject("created_by", Long.class),
                        rs.getObject("updated_by", Long.class)
                ));

        return insertedUser;
    }


    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM \"USER\"";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("full_name"),
                rs.getDate("date_of_birth"),
                rs.getInt("gender_id"),
                rs.getString("email"),
                rs.getObject("created_at") != null
                        ? rs.getTimestamp("created_at").toLocalDateTime().toLocalDate()
                        : null,
                rs.getObject("updated_at") != null
                        ? rs.getTimestamp("updated_at").toLocalDateTime().toLocalDate()
                        : null,
                rs.getObject("created_by", Long.class),
                rs.getObject("updated_by", Long.class)
        ));
    }
}
