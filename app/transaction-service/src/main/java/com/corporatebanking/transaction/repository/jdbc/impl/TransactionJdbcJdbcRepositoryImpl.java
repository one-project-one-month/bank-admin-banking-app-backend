package com.corporatebanking.transaction.repository.jdbc.impl;

import com.corporatebanking.transaction.model.Transaction;
import com.corporatebanking.transaction.repository.jdbc.TransactionJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
public class TransactionJdbcJdbcRepositoryImpl implements TransactionJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public TransactionJdbcJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transaction save(Transaction tx) {
        String sql = """
            
                INSERT INTO "Transaction" (creditAccountId, debitAccountId, amount, transactionType, transactionGroupId)
                            VALUES (?, ?, ?, ?, ?)
            """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, tx.creditAccountId());
            ps.setLong(2, tx.debitAccountId());
            ps.setBigDecimal(3, tx.amount());
            ps.setString(4, tx.transactionType());
            ps.setObject(5, tx.transactionGroupId());
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Transaction(id, tx.creditAccountId(), tx.debitAccountId(), tx.amount(), tx.transactionType(), tx.transactionGroupId()
);}
}


