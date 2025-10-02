package com.corporatebanking.transaction.repository.jdbc;

import com.corporatebanking.transaction.model.Transaction;

public interface TransactionJdbcRepository {
    Transaction save(Transaction transaction);
}
