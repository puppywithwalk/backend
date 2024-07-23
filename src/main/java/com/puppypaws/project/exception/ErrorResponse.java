package com.puppypaws.project.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class ErrorResponse {

    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }
}