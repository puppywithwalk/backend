package com.puppypaws.project.dto.Token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponseDto {
    private String accessToken;
    private Integer status;

    private TokenResponseDto(Integer status, String accessToken) {
        this.status = status;
        this.accessToken = accessToken;
    }
    public static TokenResponseDto of(Integer status, String accessToken) {
        return new TokenResponseDto(status, accessToken);
    }
}