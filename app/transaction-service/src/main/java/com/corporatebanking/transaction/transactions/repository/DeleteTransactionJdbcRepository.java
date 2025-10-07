package com.corporatebanking.transaction.transactions.repository;

public interface DeleteTransactionJdbcRepository {
    int deleteById(Long id);                // Soft delete
}
