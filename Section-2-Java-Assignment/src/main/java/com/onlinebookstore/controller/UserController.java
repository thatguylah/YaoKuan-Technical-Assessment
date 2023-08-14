package com.onlinebookstore.controller;

import com.onlinebookstore.model.entity.User;
import com.onlinebookstore.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.onlinebookstore.model.dto.UserRegistrationDTO;
import com.onlinebookstore.model.dto.UserLoginDTO;
import org.springframework.http.ResponseEntity;
import io.swagger.annotations.*;


import javax.validation.Valid;
import java.util.List;

@Api(tags = "User Management")  // <-- Class Level Annotation
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Registers a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully registered user"),
            @ApiResponse(code = 400, message = "Invalid input or user already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO userDTO) {
        User registerdUser = userService.registerUser(userDTO);
        return ResponseEntity.ok(registerdUser);
    }

    @ApiOperation(value = "Authenticates a user and retrieves a JWT token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully authenticated and retrieved JWT token"),
            @ApiResponse(code = 401, message = "Invalid credentials"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDTO userLoginDto) {
        String jwtToken = userService.authenticateAndRetrieveJWT(userLoginDto);
        return ResponseEntity.ok(jwtToken);
    }

    @ApiOperation(value = "Retrieves a list of all registered users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of users")
    })
    @GetMapping
    private List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Endpoints for login and other user functionalities can be added later.
}
