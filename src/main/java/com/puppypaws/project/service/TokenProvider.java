package com.puppypaws.project.service;

import com.puppypaws.project.entity.Token;
import com.puppypaws.project.repository.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private final Key key;
    private final static long ACCESS_TOKEN_EXPIRE_TIME  = 1000L * 60L * 3L;
    private final static long REFRESH_TOKEN_EXPIRE_TIME  = 1000L * 60L * 60L * 8L;
    @Autowired
    private TokenRepository tokenRepository;
    public TokenProvider(){
        byte[] keyBytes = Decoders.BASE64.decode("dGxxa2Zyb3dod3JreHNwdGdkZmdzZGZnc2RmZ3NkZmc=");
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public void generateRefreshToken(Authentication authentication, String accessToken) {
        String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
//        tokenRepository.saveToken(new Token(accessToken, refreshToken));
        tokenRepository.saveToken(accessToken, refreshToken);
    }
    public String generateAccessToken(Authentication authentication){
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
    }
    public String generateToken(Authentication authentication, long expireTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);
        String authorties = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder()
                        .claim("auth", authorties)
                        .claim("name", authentication.getName())
                        .setIssuedAt(now)
                        .setExpiration(expiredDate)
                        .signWith(key)
                        .compact();
    }
    public String reissueAccessToken(String accessToken) {
        if (StringUtils.hasText(accessToken)) {
            Optional<Token> tokenOptional = tokenRepository.findByAccessToken(accessToken);
            if (tokenOptional.isPresent()) {
                Token token = tokenOptional.get();
                String refreshToken = token.getRefreshToken();

                if (validationToken(refreshToken)) {
                    String reissueAccessToken = generateToken(getAuthentication(refreshToken), ACCESS_TOKEN_EXPIRE_TIME);
//                    tokenRepository.delete(new Token(accessToken, refreshToken));
//                    tokenRepository.save(new Token(reissueAccessToken, refreshToken));
                    tokenRepository.updateToken(reissueAccessToken, refreshToken);
                    return reissueAccessToken;
                }
            }
        }
        return null;
    }

    private Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
    public Authentication getAuthentication(String accesToken){

        Claims claims = parseClaims(accesToken);

        if(claims.get("auth") == null){
            throw new RuntimeException("ERR");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        return new UsernamePasswordAuthenticationToken(claims.get("name"),"",authorities);
    }
    public String resolveToken(String bearerToken){
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validationToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e){
            log.info("Invalid");
        } catch (ExpiredJwtException e) {
            log.info("Expired");
        } catch (UnsupportedJwtException e){
            log.info("Unsupported");
        } catch (IllegalArgumentException e){
            log.info("empty");
        }
        return false;
    }
}