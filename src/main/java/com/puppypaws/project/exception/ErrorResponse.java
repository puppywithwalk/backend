package com.puppypaws.project.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private int statusCode;

    public ErrorResponse(ErrorCode code) {
        this.statusCode = code.getStatusCode();
        this.message = code.getMessage();
    }
}