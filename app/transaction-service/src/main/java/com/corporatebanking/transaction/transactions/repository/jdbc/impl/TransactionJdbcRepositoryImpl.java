package com.corporatebanking.transaction.transactions.repository.jdbc.impl;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.corporatebanking.transaction.transactions.models.AccountTypeData;
import com.corporatebanking.transaction.transactions.models.TransactionData;
import com.corporatebanking.transaction.transactions.repository.jdbc.TransactionJdbcRepository;

@Repository
public class TransactionJdbcRepositoryImpl implements TransactionJdbcRepository{

    private final JdbcTemplate jdbcTemplate;

    public TransactionJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public final RowMapper<TransactionData> transactionRowMapper = (rs, rowNum) ->{

        AccountTypeData accountTypeData=null;
        Long accountTypeId = rs.getObject("at_id", Long.class);
        if(accountTypeId != null){
            accountTypeData = new AccountTypeData(
                accountTypeId,
                rs.getString("at_name")
            );
        }

        return new TransactionData(
            rs.getLong("t_id"),
            accountTypeData,
            rs.getString("t_account_number"),
            rs.getString("t_name"),
            rs.getDouble("t_amount"),
            rs.getString("t_note"),
            rs.getDate("t_created_at") != null ? rs.getDate("t_created_at").toLocalDate(): null,
            rs.getDate("t_updated_at") != null ? rs.getDate("t_updated_at").toLocalDate() : null
        );
    };

    @Override
    public List<TransactionData> findAll() {
        String sql = """
        SELECT 
            t.id              AS t_id,
            t.account_number  AS t_account_number,
            t.name            AS t_name,
            t.amount          AS t_amount,
            t.note            AS t_note,
            t.created_at      AS t_created_at,
            t.updated_at      AS t_updated_at,
            at.id             AS at_id,
            at.name           AS at_name
        FROM transactions t
        LEFT JOIN account_type at ON at.id = t.account_type_id
        """;
        return jdbcTemplate.query(sql, transactionRowMapper);
    }

}
