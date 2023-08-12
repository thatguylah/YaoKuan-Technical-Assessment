package com.onlinebookstore.controller;

import com.onlinebookstore.model.entity.User;
import com.onlinebookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping
    private List<User> getAllUsers() {
        return bookService.getAllUsers();
    }

    // Endpoints for login and other user functionalities can be added later.
}
