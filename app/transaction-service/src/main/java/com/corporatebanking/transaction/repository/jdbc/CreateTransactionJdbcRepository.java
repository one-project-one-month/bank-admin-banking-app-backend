package com.corporatebanking.transaction.repository.jdbc;

import com.corporatebanking.transaction.models.CreateTransactionData;

public interface CreateTransactionJdbcRepository {
    CreateTransactionData save(CreateTransactionData createTransactionData);
}
