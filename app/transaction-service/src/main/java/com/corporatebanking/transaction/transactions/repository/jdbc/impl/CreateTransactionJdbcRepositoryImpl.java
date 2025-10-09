package com.corporatebanking.transaction.transactions.repository.jdbc.impl;

import com.corporatebanking.transaction.transactions.models.CreateTransactionAccountTypeData;
import com.corporatebanking.transaction.transactions.models.CreateTransactionData;
import com.corporatebanking.transaction.transactions.repository.jdbc.CreateTransactionJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CreateTransactionJdbcRepositoryImpl implements CreateTransactionJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public CreateTransactionJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CreateTransactionData> transactionRowMapper = (rs, rowNum) -> {
        CreateTransactionAccountTypeData createTransactionAccountTypeData = null;
        Long accountTypeId = rs.getObject("at_id", Long.class);
        if (accountTypeId != null) {
            createTransactionAccountTypeData = new CreateTransactionAccountTypeData(
                    accountTypeId,
                    rs.getString("at_name")
            );
        }

        return new CreateTransactionData(
                rs.getLong("t_id"),
                createTransactionAccountTypeData,
                rs.getString("t_account_number"),
                rs.getString("t_name"),
                rs.getDouble("t_amount"),
                rs.getString("t_note"),
                rs.getDate("t_created_at") != null ? rs.getDate("t_created_at").toLocalDate() : null,
                rs.getDate("t_updated_at") != null ? rs.getDate("t_updated_at").toLocalDate() : null
        );
    };


    @Override
    public CreateTransactionData save(CreateTransactionData createTransactionData) {
        String insertSql = """
        INSERT INTO transactions (account_type_id, account_number, name, amount, note, created_at, updated_at)
        VALUES (?, ?, ?, ?, ?, CURRENT_DATE, CURRENT_DATE)
        RETURNING id
        """;

        Long generatedId = jdbcTemplate.queryForObject(insertSql, Long.class,
                createTransactionData.accountType().id(),
                createTransactionData.accountNumber(),
                createTransactionData.name(),
                createTransactionData.amount(),
                createTransactionData.note());

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