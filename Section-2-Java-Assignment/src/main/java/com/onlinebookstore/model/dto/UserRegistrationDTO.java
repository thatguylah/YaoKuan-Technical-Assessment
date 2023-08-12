package com.onlinebookstore.model.dto;
import com.onlinebookstore.model.entity.UserRole;

public class UserRegistrationDTO extends UserCredentialsDTO {
    private UserRole role;
    private String email;

    public UserRole getRole(){
        return role;
    }
    public void setRole(UserRole role){
        this.role = role;
    }


    // Additional fields or methods specific to registration, if any
}