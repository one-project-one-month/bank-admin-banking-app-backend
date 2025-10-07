package com.corporatebanking.transaction.repository.jdbc;

import com.corporatebanking.transaction.model.DepositTranxDto;
import com.corporatebanking.transaction.model.DepositTranxRequest;

public interface DepositTranxJdbcRepository {

    DepositTranxDto getDepositTranxById(Long id);
    int update(Long id, DepositTranxRequest request);
}
