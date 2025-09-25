package com.corporatebanking.user.features.organizations.repository.jdbc;

import java.util.List;
import java.util.Optional;

import com.corporatebanking.user.features.organizations.models.OrganizationData;

public interface OrganizationJdbcRepository {
    OrganizationData save(OrganizationData organization);
    Optional<OrganizationData> findById(Long id);
    List<OrganizationData> findAll();
    Optional<OrganizationData> update(OrganizationData organization);
    void deleteById(Long id);
}
