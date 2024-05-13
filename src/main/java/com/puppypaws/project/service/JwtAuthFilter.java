package com.puppypaws.project.service;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private static final String REFRESH_TOKEN_URI = "/token/refresh";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenProvider.resolveToken(request);

        if (shouldSkipFilter(request, token)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!tokenProvider.validationToken(token)) {
            throw new JwtException("토큰 만료");
        }

        setAuthentication(token);
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String token){
        Authentication authentication = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean shouldSkipFilter(HttpServletRequest request, String token) {
        return !StringUtils.hasText(token) || isRefreshTokenRequest(request);
    }

    private boolean isRefreshTokenRequest(HttpServletRequest request) {
        return request.getRequestURI().contains(REFRESH_TOKEN_URI);
    }
}
