package com.corporatebanking.auth.repository;

import com.corporatebanking.auth.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Optional<User> findByUsername(String username,String role) {
        return find("username", username).filter("role", role).firstResultOptional();
    }
}
