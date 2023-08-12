//package com.onlinebookstore.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//
//@Configuration
//public class SecurityConfig extends AbstractHttpConfigurer<SecurityConfig, WebSecurityConfigurer<HttpSecurity>> {
//
//    @Override
//    public void init(WebSecurityConfigurer<HttpSecurity> http) throws Exception {
//        http
//                .csrf().disable() // Disabling CSRF
//                .authorizeRequests()
//                .anyRequest().permitAll(); // Allowing all requests
//    }
//}
