//***************************************************************************************
//
//     Filename: WebSecurityConfig.java
//     Author: Kyle McColgan
//     Date: 21 November 2024
//     Description: This file contains the Spring Security application configuration.
//
//***************************************************************************************

package com.kmccol1.to_do_app.security.config;

import com.kmccol1.to_do_app.Services.UserService;
import com.kmccol1.to_do_app.security.JwtAuthenticationFilter;
import com.kmccol1.to_do_app.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

//***************************************************************************************

@Configuration
@EnableWebSecurity
public class WebSecurityConfig
{
    private final JwtUtils jwtUtils;

    @Autowired
    @Lazy
    private final UserService userService;

    public WebSecurityConfig(JwtUtils jwtUtils, UserService userService)
    {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        var corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type", "*"));
        corsConfig.setAllowCredentials(true);
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/todos/**").hasRole("USER")
                        //.requestMatchers(HttpMethod.DELETE, "/api/todos/**").permitAll()
                        .anyRequest().authenticated()
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtils, userService), UsernamePasswordAuthenticationFilter.class);  // Use the bean here
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter()
    {
        return new JwtAuthenticationFilter(jwtUtils, userService);
    }

    @Bean
    @Lazy
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception
    {
        return authConfig.getAuthenticationManager();
    }
}

//***************************************************************************************