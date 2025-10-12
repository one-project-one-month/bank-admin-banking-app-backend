package com.corporatebanking.transaction.transactions.repository.jdbc;

import java.util.List;

import com.corporatebanking.transaction.transactions.models.TransactionData;

public interface TransactionJdbcRepository {
    List<TransactionData> findAll();
}
