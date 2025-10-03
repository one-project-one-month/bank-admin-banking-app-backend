package com.corporatebanking.transaction.transactions.repository.jdbc;

import com.corporatebanking.transaction.transactions.models.TransactionData;

public interface TransactionJdbcRepository {
    TransactionData save(TransactionData transactionData);
}
