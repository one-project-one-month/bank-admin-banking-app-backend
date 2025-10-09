package com.corporatebanking.transaction.transactions.repository.jdbc;

import com.corporatebanking.transaction.transactions.models.CreateTransactionData;

public interface CreateTransactionJdbcRepository {
    CreateTransactionData save(CreateTransactionData createTransactionData);
}
