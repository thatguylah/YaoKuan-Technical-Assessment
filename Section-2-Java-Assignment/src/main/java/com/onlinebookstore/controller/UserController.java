package com.onlinebookstore.controller;

import com.onlinebookstore.model.entity.User;
import com.onlinebookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.onlinebookstore.model.dto.UserRegistrationDTO;
import com.onlinebookstore.model.dto.UserLoginDTO;
import org.springframework.http.ResponseEntity;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param userDTO The user registration data transfer object.
     * @return ResponseEntity containing the registered user or an error message.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO userDTO) {
        User registerdUser = userService.registerUser(userDTO);
        return ResponseEntity.ok(registerdUser);
    }

    /**
     * Authenticates a user and retrieves a JWT token.
     *
     * @param userLoginDto The user login data transfer object.
     * @return ResponseEntity containing the JWT token or an error message.
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDTO userLoginDto) {
        String jwtToken = userService.authenticateAndRetrieveJWT(userLoginDto);
        return ResponseEntity.ok(jwtToken);
    }


    @GetMapping
    private List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Endpoints for login and other user functionalities can be added later.
}
