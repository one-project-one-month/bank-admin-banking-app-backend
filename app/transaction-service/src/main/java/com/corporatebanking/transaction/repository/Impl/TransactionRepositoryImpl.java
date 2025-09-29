package com.corporatebanking.transaction.repository.Impl;

import com.corporatebanking.transaction.model.Transaction;
import com.corporatebanking.transaction.model.TransactionGroup;
import com.corporatebanking.transaction.repository.TransactionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transaction createTransaction(Transaction tx) {
        String sql = """
                INSERT INTO transaction
                (creditAccountId, debitAccountId, amount, transactionType, transactionGroupId)
                VALUES (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, tx.creditAccountId());
            ps.setLong(2, tx.debitAccountId());
            ps.setBigDecimal(3, tx.amount());
            ps.setString(4, tx.transactionType());
            ps.setLong(5, tx.transactionGroupId());
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Transaction(id, tx.creditAccountId(), tx.debitAccountId(), tx.amount(), tx.transactionType(), tx.transactionGroupId());
    }

    @Override
    public TransactionGroup saveTransactionGroup(TransactionGroup group) {
        String sql = """
                INSERT INTO transaction_group
                (creditorOrg, totalAmount, transactionType, fileUrl, fileStatus, ruleSetId, groupId, fileName, makerId)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, group.creditorOrg());
            ps.setBigDecimal(2, group.totalAmount());
            ps.setString(3, group.transactionType());
            ps.setString(4, group.fileUrl());
            ps.setString(5, group.fileStatus());
            ps.setLong(6, group.ruleSetId());
            ps.setLong(7, group.groupId());
            ps.setString(8, group.fileName());
            ps.setLong(9, group.makerId());
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new TransactionGroup(id,
                group.creditorOrg(),
                group.totalAmount(),
                group.transactionType(),
                group.fileUrl(),
                group.fileStatus(),
                group.ruleSetId(),
                group.groupId(),
                group.fileName(),
                group.makerId());
    }

    @Override
    public TransactionGroup findTransactionGroupById(Long id) {
        String sql = "SELECT * FROM TransactionGroup WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new TransactionGroup(
                        rs.getLong("id"),
                        rs.getLong("creditorOrg"),
                        rs.getBigDecimal("totalAmount"),
                        rs.getString("transactionType"),
                        rs.getString("fileUrl"),
                        rs.getString("fileStatus"),
                        rs.getObject("ruleSetId", Long.class),
                        rs.getObject("groupId", Long.class),
                        rs.getString("fileName"),
                        rs.getLong("makerId")
                ), id);
    }
}
