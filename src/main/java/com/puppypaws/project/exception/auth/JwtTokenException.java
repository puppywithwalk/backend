package com.puppypaws.project.exception.auth;

import com.puppypaws.project.exception.CustomException;
import com.puppypaws.project.exception.ErrorCode;

public class JwtTokenException extends CustomException {
    public JwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}