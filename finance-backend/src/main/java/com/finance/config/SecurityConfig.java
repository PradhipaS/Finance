package com.finance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
                // Login is open to everyone
                .antMatchers("/api/auth/**").permitAll()

                // Swagger UI - open to everyone
                .antMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

                // Only ADMIN can manage users
                .antMatchers("/api/users/**").hasRole("ADMIN")

                // All roles can view records
                .antMatchers(HttpMethod.GET, "/api/records/**").hasAnyRole("ADMIN", "ANALYST", "VIEWER")

                // Only ADMIN can create, update, delete records
                .antMatchers(HttpMethod.POST, "/api/records/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/records/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/records/**").hasRole("ADMIN")

                // ADMIN and ANALYST can see dashboard
                .antMatchers("/api/dashboard/**").hasAnyRole("ADMIN", "ANALYST")

                // Everything else needs login
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
