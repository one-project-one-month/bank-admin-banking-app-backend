package com.corporatebanking.transaction.transactions.repository.jdbc.impl;

import com.corporatebanking.transaction.transactions.models.AccountTypeData;
import com.corporatebanking.transaction.transactions.models.TransactionData;
import com.corporatebanking.transaction.transactions.repository.jdbc.TransactionJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionJdbcRepositoryImpl implements TransactionJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<TransactionData> transactionRowMapper = (rs, rowNum) -> {
        AccountTypeData accountTypeData = null;
        Long accountTypeId = rs.getObject("at_id", Long.class);
        if (accountTypeId != null) {
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
                rs.getDate("t_created_at") != null ? rs.getDate("t_created_at").toLocalDate() : null,
                rs.getDate("t_updated_at") != null ? rs.getDate("t_updated_at").toLocalDate() : null
        );
    };


    @Override
    public TransactionData save(TransactionData transactionData) {
        String insertSql = """
        INSERT INTO transactions (account_type_id, account_number, name, amount, note, created_at, updated_at)
        VALUES (?, ?, ?, ?, ?, CURRENT_DATE, CURRENT_DATE)
        RETURNING id
        """;

        Long generatedId = jdbcTemplate.queryForObject(insertSql, Long.class,
                transactionData.accountType().id(),
                transactionData.accountNumber(),
                transactionData.name(),
                transactionData.amount(),
                transactionData.note());

        String selectSql = """
        SELECT 
            t.id AS t_id,
            t.account_number AS t_account_number,
            t.name AS t_name,
            t.amount AS t_amount,
            t.note AS t_note,
            t.created_at AS t_created_at,
            t.updated_at AS t_updated_at,
            at.id AS at_id,
            at.name AS at_name
        FROM transactions t
        LEFT JOIN account_type at ON at.id = t.account_type_id
        WHERE t.id = ?
        """;

        return jdbcTemplate.queryForObject(selectSql, transactionRowMapper, generatedId);
    }
}