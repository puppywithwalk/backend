package com.puppypaws.project.controller;

import com.puppypaws.project.dto.Token.TokenResponseDto;
import com.puppypaws.project.entity.Token;
import com.puppypaws.project.repository.TokenRepository;
import com.puppypaws.project.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    @GetMapping("/signin/getToken")
    public String test(){
        return "ok";
    }

    @GetMapping("/token/refresh")
    @ResponseBody
    public ResponseEntity<TokenResponseDto> refresh(@RequestHeader("Authorization") final String accessToken){
        String resolveToken = tokenProvider.resolveToken(accessToken);
        Optional<Token> refreshToken = tokenRepository.findByAccessToken(resolveToken);

        if (refreshToken.isPresent() && tokenProvider.validationToken(refreshToken.get().getRefreshToken())) {
             String newAccessToken = tokenProvider.reissueAccessToken(resolveToken);
            return ResponseEntity.ok(TokenResponseDto.of(200, newAccessToken));
        }

        return ResponseEntity.badRequest().body(TokenResponseDto.of(400, null));
    }
}
