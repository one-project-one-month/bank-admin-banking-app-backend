package com.learning.sysname.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name="user")
@Getter
@Setter
public class User implements Serializable {
    @Id
    private Long id;

    @Column(name="username")
    private String username;

    @Column(name="fullname")
    private String fullName;
}
