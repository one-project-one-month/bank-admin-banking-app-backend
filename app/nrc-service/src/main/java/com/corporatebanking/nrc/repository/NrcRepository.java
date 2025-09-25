package com.corporatebanking.nrc.repository;

import com.corporatebanking.nrc.model.NrcCode;
import com.corporatebanking.nrc.model.NrcCodeValue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NrcRepository {

    private final JdbcTemplate jdbcTemplate;

    public NrcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<NrcCode> nrcCodeRowMapper = (rs, rowNum) -> new NrcCode(
            rs.getLong("id"),
            rs.getString("name")
    );

    private final RowMapper<NrcCodeValue> nrcCodeValueRowMapper = (rs, rowNum) -> new NrcCodeValue(
            rs.getLong("id"),
            rs.getLong("code_id"),
            rs.getString("value")
    );

    public List<NrcCode> findAllNrcCodes() {
        return jdbcTemplate.query("SELECT id, name FROM nrc_code", nrcCodeRowMapper);
    }

    public List<NrcCodeValue> findValuesByCodeName(String codeName) {
        String sql = "SELECT v.id, v.code_id, v.value FROM nrc_code_value v " +
                     "JOIN nrc_code c ON v.code_id = c.id WHERE c.name = ?";
        return jdbcTemplate.query(sql, nrcCodeValueRowMapper, codeName);
    }
}
