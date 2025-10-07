package com.corporatebanking.transaction.repository.jdbc;

import java.util.List;

import com.corporatebanking.transaction.models.AccountTypeOptionData;

public interface DepositeTemplateJdbcRepository {
    List<AccountTypeOptionData> findAllAccountTypeOptions();
}
