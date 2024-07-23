package com.puppypaws.project.controller;

import com.puppypaws.project.exception.CustomException;
import com.puppypaws.project.exception.ErrorCode;
import com.puppypaws.project.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e,  HttpServletRequest request) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("CustomException occurred:: ErrorCode: {}, Message: {}, Request URI: {}, HTTP Method: {}",
                errorCode,
                e.getMessage(),
                request.getRequestURI(),
                request.getMethod());
        ErrorResponse response = new ErrorResponse(errorCode);
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }
}