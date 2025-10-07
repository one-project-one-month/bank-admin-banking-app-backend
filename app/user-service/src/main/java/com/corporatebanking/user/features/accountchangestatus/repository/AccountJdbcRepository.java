package com.corporatebanking.user.features.accountchangestatus.repository;

import com.corporatebanking.user.features.accountchangestatus.model.Account;

import java.util.Optional;

public interface AccountJdbcRepository {
    Optional<Account> findById(Long id) ;
    void updateStatus(Long id, String newStatus);
}
