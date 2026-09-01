package com.rohit.workflow_ai.config;

import com.rohit.workflow_ai.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

    // ==========================
    // Password Encoder
    // ==========================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ==========================
    // CORS Configuration
    // ==========================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    // ==========================
    // Security Filter Chain
    // ==========================
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // ==========================
                // CORS
                // ==========================
                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource()
                        )
                )

                // ==========================
                // CSRF
                // ==========================
                .csrf(csrf -> csrf.disable())

                // ==========================
                // Stateless JWT
                // ==========================
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // ==========================
                // Authorization
                // ==========================
                .authorizeHttpRequests(auth -> auth

                        // Authentication APIs
                        .requestMatchers(
                                "/api/v1/auth/**"
                        ).permitAll()

                        // ==========================
                        // Swagger
                        // ==========================
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // ==========================
                        // Logged-in user's profile
                        // ==========================
                        .requestMatchers(
                                "/api/v1/users/me"
                        ).authenticated()

                        // ==========================
                        // User Management
                        // ==========================
                        .requestMatchers(
                                "/api/v1/users",
                                "/api/v1/users/**"
                        ).hasAnyRole(
                                "SUPER_ADMIN",
                                "COMPANY_ADMIN"
                        )

                        // ==========================
                        // Everything else
                        // ==========================
                        .anyRequest().authenticated()
                )

                // ==========================
                // JWT Filter
                // ==========================
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}