package com.onlinebookstore.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    public User() {
        // No-arg constructor
    }

    public User(String username, String passwordHash, UserRole role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }


    public String getUsername(){
        return username;
    }
    public String getPasswordHash(){
        return passwordHash;
    }

    public UserRole getUserRole(){
        return role;
    }

    public void setUserRole(UserRole role){
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
    }

    // Add roles, email, or other attributes if required.

    // Standard constructors, getters, setters, etc.
}