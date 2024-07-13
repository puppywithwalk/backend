package com.puppypaws.project.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NO_USER(401, "사용자 계정을 확인해주세요."),
    EXPIRED_TOKEN(401, "액세스토큰이 만료되었습니다."),
    NOT_FOUND(404, "ID를 확인해주세요."),
    NOT_AUTHOR(403, "작성자가 아닙니다.");

    private final int statusCode;
    private final String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
