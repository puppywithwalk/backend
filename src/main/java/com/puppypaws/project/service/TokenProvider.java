package com.puppypaws.project.service;

import com.puppypaws.project.exception.ErrorCode;
import com.puppypaws.project.exception.auth.*;
import com.puppypaws.project.model.CustomOAuth2User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {
    private final static long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60L * 5L;
    private final static long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60L * 30L;
    private final RedisService redisService;
    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    public void generateRefreshToken(Authentication authentication, String accessToken) {
        String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
        redisService.storeTokenWithExpiry(accessToken, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String generateToken(Authentication authentication, long expireTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(String.valueOf(principal.getId()))
                .claim("role", authorities)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(key)
                .compact();
    }

    public String reissueAccessToken(String oldAccessToken, String refreshToken) {
        if (StringUtils.hasText(oldAccessToken)) {
                String reissueAccessToken = generateToken(getAuthentication(refreshToken), ACCESS_TOKEN_EXPIRE_TIME);
                redisService.updateReissueAccessToken(reissueAccessToken, refreshToken, oldAccessToken);
                return reissueAccessToken;
        }
        throw new JwtTokenException(ErrorCode.INVALID_TOKEN);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("role") == null) {
            throw new JwtTokenException(ErrorCode.INVALID_TOKEN);
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("role").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        CustomOAuth2User principal = new CustomOAuth2User(Long.valueOf(claims.getSubject()));
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return "";
    }
    public boolean validationToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("SecurityException 또는 MalformedJwtException 발생: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("ExpiredJwtException 발생: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("UnsupportedJwtException 발생: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("IllegalArgumentException 발생: {}", e.getMessage());
        }
        return false;
    }
}