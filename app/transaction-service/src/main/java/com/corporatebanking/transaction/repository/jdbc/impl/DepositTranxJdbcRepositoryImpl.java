package com.corporatebanking.transaction.repository.jdbc.impl;

import com.corporatebanking.transaction.model.AccountTypeDto;
import com.corporatebanking.transaction.model.DepositTranxDto;
import com.corporatebanking.transaction.model.DepositTranxRequest;
import com.corporatebanking.transaction.repository.jdbc.DepositTranxJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DepositTranxJdbcRepositoryImpl implements DepositTranxJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public DepositTranxJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public RowMapper<DepositTranxDto> depositTranxRowMapper = (rs, rowNum) -> {

        AccountTypeDto accountType = new AccountTypeDto(
                rs.getLong("account_type_id"),
                rs.getString("account_type_name")
        );

        return new DepositTranxDto(
                rs.getLong("transaction_id"),
                accountType,
                rs.getString("account_number"),
                rs.getString("name"),
                rs.getDouble("amount"),
                rs.getString("note"),
                rs.getDate("created_at").toLocalDate(),
                rs.getDate("updated_at") != null ? rs.getDate("updated_at").toLocalDate() : null
        );
    };

    @Override
    public DepositTranxDto getDepositTranxById(Long id) {
        String sql= """
                SELECT
                    t.id AS transaction_id,
                    t.account_number,
                    t.name,
                    t.amount,
                    t.note,
                    t.created_at,
                    t.updated_at,
                    a.id AS account_type_id,
                    a.name AS account_type_name
                FROM transactions t
                JOIN account_type a ON t.account_type_id = a.id
                WHERE t.id = ?
                """;
        return jdbcTemplate.queryForObject(sql, depositTranxRowMapper, id);
    }

    @Override
    public int update(Long id, DepositTranxRequest request) {
        String sql="UPDATE transactions SET account_type_id=?,account_number=?,name=?, amount=?, note=?, updated_at=NOW()::DATE WHERE id=?";
        int isUpdated = jdbcTemplate.update(
                sql,
                request.accountTypeId(),
                request.accountNumber(),
                request.name(),
                request.amount(),
                request.note(),
                id
        );

        return isUpdated;
    }

}
