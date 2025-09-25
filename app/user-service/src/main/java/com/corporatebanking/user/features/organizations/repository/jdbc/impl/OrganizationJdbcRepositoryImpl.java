package com.corporatebanking.user.features.organizations.repository.jdbc.impl;

import com.corporatebanking.user.features.organizations.models.OrganizationData;
import com.corporatebanking.user.features.organizations.repository.jdbc.OrganizationJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class OrganizationJdbcRepositoryImpl implements OrganizationJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrganizationJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<OrganizationData> organizationRowMapper = (rs, rowNum) -> new OrganizationData(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("shortcode"),
            rs.getString("address"),
            rs.getString("country"),
            rs.getDate("created_at") != null ? rs.getDate("created_at").toLocalDate() : null,
            rs.getDate("updated_at") != null ? rs.getDate("updated_at").toLocalDate() : null,
            rs.getObject("created_by", Long.class),
            rs.getObject("updated_by", Long.class)
    );

    @Override
    public OrganizationData save(OrganizationData organization) {
        String sql = "INSERT INTO organization (name, shortcode, address, country, created_at, created_by) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, organization.name());
            ps.setString(2, organization.shortCode());
            ps.setString(3, organization.address());
            ps.setString(4, organization.country());
            ps.setDate(5, Date.valueOf(LocalDate.now()));
            if (organization.createdBy() != null) {
                ps.setLong(6, organization.createdBy());
            } else {
                ps.setNull(6, Types.BIGINT);
            }
            return ps;
        }, keyHolder);

        long generatedId = (long) keyHolder.getKeys().get("id");
        return findById(generatedId).orElse(null);
    }

    @Override
    public Optional<OrganizationData> findById(Long id) {
        String sql = "SELECT * FROM organization WHERE id = ?";
        return jdbcTemplate.query(sql, organizationRowMapper, id).stream().findFirst();
    }

    @Override
    public List<OrganizationData> findAll() {
        String sql = "SELECT * FROM organization";
        return jdbcTemplate.query(sql, organizationRowMapper);
    }

    @Override
    public Optional<OrganizationData> update(OrganizationData organization) {
        String sql = "UPDATE organization SET name = ?, shortcode = ?, address = ?, country = ?, updated_at = ?, updated_by = ? WHERE id = ?";
        int updatedRows = jdbcTemplate.update(sql,
                organization.name(),
                organization.shortCode(),
                organization.address(),
                organization.country(),
                Date.valueOf(LocalDate.now()),
                organization.updatedBy(),
                organization.id());

        if (updatedRows > 0) {
            return findById(organization.id());
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM organization WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
