package com.puppypaws.project.controller;

import com.puppypaws.project.dto.Token.TokenResponseDto;
import com.puppypaws.project.service.RedisService;
import com.puppypaws.project.service.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class AuthController {
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    @GetMapping("/signin/getToken")
    public String test() {
        return "ok";
    }

    @GetMapping("/token/refresh")
    @ResponseBody
    public ResponseEntity<TokenResponseDto> refresh(HttpServletRequest httpServletRequest) {
        String resolveToken = tokenProvider.resolveToken(httpServletRequest);
        String refreshToken = redisService.findTokenByRefreshToken(resolveToken);

        if (StringUtils.hasText(refreshToken) && tokenProvider.validationToken(refreshToken)){
            String newAccessToken = tokenProvider.reissueAccessToken(resolveToken, refreshToken);
            return ResponseEntity.ok(TokenResponseDto.of(200, newAccessToken));
        } else{
            return ResponseEntity.badRequest().body(TokenResponseDto.of(400, null));
        }
    }
}
