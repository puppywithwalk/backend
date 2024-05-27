package com.puppypaws.project;

import com.puppypaws.project.exception.TokenException;
import com.puppypaws.project.service.CustomOAuth2UserService;
import com.puppypaws.project.service.JwtAuthFilter;
import com.puppypaws.project.service.OAuth2AuthenticationSuccessHandler;

import com.puppypaws.project.service.Oauth2FailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final Oauth2FailureHandler oauth2FailureHandler;
    private final JwtAuthFilter jwtAuthFilter;
    private final TokenException tokenException;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authRequest -> authRequest
                .requestMatchers("/token/refresh").permitAll()
                .requestMatchers("/signin/getToken").permitAll()
                .requestMatchers("/test").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/community").permitAll()
                .requestMatchers("/community/{id}").permitAll()
                .requestMatchers("/dogstagram").permitAll()
                .requestMatchers("/dogstagram/star-dogs").permitAll()
                .anyRequest().authenticated())

        .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig ->
                            userInfoEndpointConfig.userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                )
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenException, JwtAuthFilter.class);

        return http.build();
    }
}