package com.puppypaws.project;

import com.puppypaws.project.service.CustomOAuth2UserService;
import com.puppypaws.project.service.JwtAuthFilter;
import com.puppypaws.project.service.OAuth2AuthenticationSuccessHandler;

import com.puppypaws.project.service.Oauth2FailureHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    @Autowired
    private Oauth2FailureHandler oauth2FailureHandler;
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authRequest -> authRequest
                .requestMatchers("/login/oauth2/code/kakao").permitAll() // "/test" 엔드포인트에 대한 요청만 허용
                .requestMatchers("/login/oauth2/code/google").permitAll() // "/test" 엔드포인트에 대한 요청만 허용
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
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}