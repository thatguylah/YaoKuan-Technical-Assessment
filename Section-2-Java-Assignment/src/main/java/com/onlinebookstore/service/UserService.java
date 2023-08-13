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

    public UserService(UserRepository userRepository, JwtUtil jwtUtil, ApplicationContext ctx) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
        this.ctx = ctx;
    }

    private AuthenticationManager getAuthenticationManager() {
        return ctx.getBean("myAuthenticationManager", AuthenticationManager.class);
    }

    public User registerUser(UserRegistrationDTO userDto) {
        User user = new User(
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getRole()
        );
//        user.setUsername(userDto.getUsername());
//        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));
//        user.setUserRole(userDto.getRole());
        return userRepository.save(user);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String authenticateAndRetrieveJWT(UserLoginDTO userLoginDto) {
        Authentication auth = authenticate(
                userLoginDto.getUsername(),
                userLoginDto.getPassword()
        );
        return generateToken(auth);
    }

    public Authentication authenticate(String username, String password) {
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    public String generateToken(Authentication authentication) {
        return jwtUtil.generateToken(authentication);
    }

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
