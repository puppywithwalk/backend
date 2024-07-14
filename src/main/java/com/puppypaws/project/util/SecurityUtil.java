package com.puppypaws.project.util;

import com.puppypaws.project.exception.CustomException;
import com.puppypaws.project.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtil {

    public static Long getAuthenticatedUserId() {
        Long userId = getAuthenticatedUserIdFromContext();
        return Optional.ofNullable(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_USER));
    }
     public static Long getAuthenticatedUserIdFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Integer) {
                return ((Integer) principal).longValue();
            }
        }
        return null;
    }
}
