package com.onlinebookstore.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username",unique = true, nullable = false)
    private String username;

    @Column(name="password",nullable = false)
    private String passwordHash;

    @Column(name="role")
    private String userRole;

    public String getPasswordHash(){
        return passwordHash;
    }

    public String getUserRole(){
        return userRole;
    }

    public void setPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
    }

    // Add roles, email, or other attributes if required.

    // Standard constructors, getters, setters, etc.
}