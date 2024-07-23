package com.puppypaws.project.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {
    NO_USER(HttpStatus.UNAUTHORIZED, "사용자 계정을 확인해주세요."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "액세스토큰이 만료되었습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "ID를 확인해주세요."),
    NOT_AUTHOR(HttpStatus.FORBIDDEN, "작성자가 아닙니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    ILLEGAL_REGISTRATION_ID(HttpStatus.BAD_REQUEST, "잘못된 등록 ID입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(final HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
