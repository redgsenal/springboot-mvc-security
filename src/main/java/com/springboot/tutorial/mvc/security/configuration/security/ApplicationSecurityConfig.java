package com.springboot.tutorial.mvc.security.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class ApplicationSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails john = buildUserDetails("john", "{noop}test123", "EMPLOYEE");
        UserDetails mary = buildUserDetails("mary", "{noop}test123", "EMPLOYEE", "MANAGER");
        UserDetails susan = buildUserDetails("susan", "{noop}test123", "EMPLOYEE", "MANAGER", "ADMIN");
        return new InMemoryUserDetailsManager(john, mary, susan);
    }

    private UserDetails buildUserDetails(String username, String password, String... roles) {
        UserDetails userDetails = User.builder()
                .username(username)
                .password(password)
                .roles(roles)
                .build();
        return userDetails;
    }
}
