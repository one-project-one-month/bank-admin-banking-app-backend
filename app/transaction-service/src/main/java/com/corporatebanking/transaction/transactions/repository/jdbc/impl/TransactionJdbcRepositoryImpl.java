package com.corporatebanking.transaction.transactions.repository.jdbc.impl;

import com.corporatebanking.transaction.transactions.models.AccountTypeData;
import com.corporatebanking.transaction.transactions.models.TransactionData;
import com.corporatebanking.transaction.transactions.repository.jdbc.TransactionJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;

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
        String sql = """
            INSERT INTO transactions (account_type_id, account_number, name, amount, note, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, transactionData.accountType().id());
            ps.setString(2, transactionData.accountNumber());
            ps.setString(3, transactionData.name());
            ps.setDouble(4, transactionData.amount());
            ps.setString(5, transactionData.note());
            ps.setDate(6, Date.valueOf(transactionData.createdAt()));
            ps.setDate(7, Date.valueOf(transactionData.updatedAt()));
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;

        return new TransactionData(
                generatedId,
                transactionData.accountType(),
                transactionData.accountNumber(),
                transactionData.name(),
                transactionData.amount(),
                transactionData.note(),
                transactionData.createdAt(),
                transactionData.updatedAt()
        );
    }

}