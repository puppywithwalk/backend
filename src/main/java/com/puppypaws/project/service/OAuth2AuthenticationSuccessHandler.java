package com.puppypaws.project.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String accessToken = tokenProvider.generateAccessToken(authentication);
        tokenProvider.generateRefreshToken(authentication, accessToken);
        response.setHeader(AUTHORIZATION, "bearer " + accessToken);

        String targetUrl =  UriComponentsBuilder.fromUriString("/signin/auth")
                .queryParam("accessToken", accessToken)
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}