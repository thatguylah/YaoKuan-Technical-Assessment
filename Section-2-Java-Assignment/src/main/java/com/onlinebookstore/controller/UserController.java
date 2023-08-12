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

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO userDTO) {
        User registerdUser = userService.registerUser(userDTO);
        return ResponseEntity.ok(registerdUser);
    }


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
