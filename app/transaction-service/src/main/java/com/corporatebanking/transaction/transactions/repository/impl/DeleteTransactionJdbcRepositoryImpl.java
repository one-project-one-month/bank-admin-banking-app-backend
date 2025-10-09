package com.corporatebanking.transaction.transactions.repository.impl;
import com.corporatebanking.transaction.transactions.repository.DeleteTransactionJdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeleteTransactionJdbcRepositoryImpl implements DeleteTransactionJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public DeleteTransactionJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int deleteById(Long id) {
        String sql = "UPDATE transactions SET deleted = TRUE WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
