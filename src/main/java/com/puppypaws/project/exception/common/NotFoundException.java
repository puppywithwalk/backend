package com.puppypaws.project.exception.common;

import com.puppypaws.project.exception.CustomException;
import com.puppypaws.project.exception.ErrorCode;

public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}