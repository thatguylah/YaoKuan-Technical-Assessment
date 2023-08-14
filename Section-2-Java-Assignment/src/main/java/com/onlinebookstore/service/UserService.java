package com.onlinebookstore.service;

import com.onlinebookstore.model.entity.User;
import com.onlinebookstore.model.dto.UserLoginDTO;
import com.onlinebookstore.model.dto.UserRegistrationDTO;
import com.onlinebookstore.repository.UserRepository;
import com.onlinebookstore.util.JwtUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ApplicationContext ctx;
    /**
     * Constructor to initialize dependencies for UserService.
     * This is needed to authenticate users created in our user pool and validate their JWT.
     *
     * @param userRepository the repository to manage users.
     * @param jwtUtil utility to handle JWT operations.
     * @param ctx the ApplicationContext to get beans.
     */
    public UserService(UserRepository userRepository, JwtUtil jwtUtil, ApplicationContext ctx) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
        this.ctx = ctx;
    }

    private AuthenticationManager getAuthenticationManager() {
        return ctx.getBean("myAuthenticationManager", AuthenticationManager.class);
    }

    /**
     * Register a new user to the system.
     *
     * @param userDto the data transfer object containing user registration data.
     * @return the registered user entity.
     */

    public User registerUser(UserRegistrationDTO userDto) {
        User user = new User(
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getRole()
        );
        return userRepository.save(user);
    }

    /**
     * Retrieve all users from the system.
     *
     * @return a list of all user entities.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Authenticate a user and generate a JWT token for them.
     *
     * @param userLoginDto the data transfer object containing user login data.
     * @return a JWT token string.
     */
    public String authenticateAndRetrieveJWT(UserLoginDTO userLoginDto) {
        Authentication auth = authenticate(
                userLoginDto.getUsername(),
                userLoginDto.getPassword()
        );
        return generateToken(auth);
    }
    /**
     * Helper function to authenticate users
     * Not meant to be used directly, rather used through authenticateAndRetrieveJWT
     * Same as generateToken below.
     */
    public Authentication authenticate(String username, String password) {
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    public String generateToken(Authentication authentication) {
        return jwtUtil.generateToken(authentication);
    }

    /**
     * loadUserByUsername is a REQUIRED abstract function that needs to be implemented for Spring Boot Security.
     * It is not used anywhere in our application code, but cannot be removed!!!
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                new ArrayList<>()   // This represents the roles/authorities. Empty list for now.
        );
    }
}
