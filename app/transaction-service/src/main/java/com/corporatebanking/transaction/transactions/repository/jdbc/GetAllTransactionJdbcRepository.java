package com.corporatebanking.transaction.transactions.repository.jdbc;

import java.util.List;

import com.corporatebanking.transaction.transactions.models.GetAllTransactionData;

public interface GetAllTransactionJdbcRepository {
    List<GetAllTransactionData> findAll();
}
