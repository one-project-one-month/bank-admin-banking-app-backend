package com.corporatebanking.user.features.accountchangestatus.repository.impl;

import com.corporatebanking.user.features.accountchangestatus.model.Account;
import com.corporatebanking.user.features.accountchangestatus.repository.AccountJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountRepositoryImpl implements AccountJdbcRepository {

    final private JdbcTemplate jdbcTemplate;

    public AccountRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Account> findById(Long id) {
        final String sql = "SELECT * FROM accounts WHERE id = ?";
        try {
            Account account = jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new Account(rs.getLong("id"), rs.getString("status")),
                    id
            );
            return Optional.ofNullable(account);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void updateStatus(Long id, String newStatus) {
        final String sql = "UPDATE accounts SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, newStatus, id);
    }
}


