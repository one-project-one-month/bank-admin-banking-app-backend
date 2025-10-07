package com.corporatebanking.transaction.repository.jdbc.impl;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.corporatebanking.transaction.models.AccountTypeOptionData;
import com.corporatebanking.transaction.repository.jdbc.DepositeTemplateJdbcRepository;

@Repository
public class DepositeTemplateJdbcRepositoryImpl implements DepositeTemplateJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public DepositeTemplateJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public final RowMapper<AccountTypeOptionData> accountTypeOptionRowMapper = (rs,
            rowNum) -> new AccountTypeOptionData(
                    rs.getLong("id"),
                    rs.getString("name"));

    @Override
    public List<AccountTypeOptionData> findAllAccountTypeOptions() {
        String sql = "SELECT id, name FROM account_type";
        return jdbcTemplate.query(sql, accountTypeOptionRowMapper);
    }

}
