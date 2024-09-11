package com.springboot.tutorial.mvc.security.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    // authentication use by JDBC using custom tables members and roles
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("select user_id, pw, active from members where user_id=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select user_id, role from roles where user_id=?");
        return jdbcUserDetailsManager;
    }

    // authentication use by JDBC using default tables users and authorities
    // @Bean
    // public UserDetailsManager userDetailsManager(DataSource dataSource){
    //     return new JdbcUserDetailsManager(dataSource);
    // }

    // use for hard-coded authentication
    // @Bean
    //  public InMemoryUserDetailsManager userDetailsManager() {
    //      UserDetails user1 = buildUserDetails("greg", "{noop}test123", "EMPLOYEE");
    //      UserDetails user2 = buildUserDetails("jason", "{noop}test123", "EMPLOYEE", "MANAGER");
    //      UserDetails user3  = buildUserDetails("kate", "{noop}test123", "EMPLOYEE", "MANAGER", "ADMIN");
    //      return new InMemoryUserDetailsManager(user1, user2, user3);
    //  }

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

    private UserDetails buildUserDetails(String username, String password, String... roles) {
        return User.builder()
                .username(username)
                .password(password)
                .roles(roles)
                .build();
    }
}
