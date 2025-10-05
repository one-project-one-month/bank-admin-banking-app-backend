package com.corporatebanking.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class    User {
    @Id
    public Long id;

    public String username;
    public String password;
    public String email;
    // public String role;
    // testing for workflow
}
