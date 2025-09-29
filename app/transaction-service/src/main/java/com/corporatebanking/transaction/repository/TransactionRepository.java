package com.corporatebanking.transaction.repository;

import com.corporatebanking.transaction.model.Transaction;
import com.corporatebanking.transaction.model.TransactionGroup;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Transaction createTransaction(Transaction tx);
    TransactionGroup saveTransactionGroup(TransactionGroup txg);
    TransactionGroup findTransactionGroupById(Long id);
}
