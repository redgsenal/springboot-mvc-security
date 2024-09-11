package com.springboot.tutorial.mvc.security.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/styles/**", "/scripts/**").permitAll()
                        .requestMatchers("/showLoginPage").permitAll()
                        .requestMatchers("/").hasRole("EMPLOYEE")
                        .requestMatchers("/leaders/**").hasRole("MANAGER")
                        .requestMatchers("/systems/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
        )
                .formLogin(
                        form
                        -> form
                                .loginPage("/showLoginPage")
                                .loginProcessingUrl("/authenticateUser")
                                .permitAll()
                )
                .logout(logout
                        -> logout
                        .addLogoutHandler(
                                new HeaderWriterLogoutHandler(
                                        new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.CACHE, ClearSiteDataHeaderWriter.Directive.COOKIES, ClearSiteDataHeaderWriter.Directive.STORAGE)
                                )
                        )
                        .permitAll()
                )
                .exceptionHandling(configurer -> configurer.accessDeniedPage("/access-denied"))
                .csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

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
