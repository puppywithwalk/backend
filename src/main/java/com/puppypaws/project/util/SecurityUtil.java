package com.puppypaws.project.util;

import com.puppypaws.project.exception.ErrorCode;
import com.puppypaws.project.exception.common.NotFoundException;
import com.puppypaws.project.model.CustomOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtil {

    public static Long getAuthenticatedUserId() {
        Long userId = getAuthenticatedUserIdFromContext();
        return Optional.ofNullable(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NO_USER));
    }
     public static Long getAuthenticatedUserIdFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            return principal.getId();
        }
        return null;
    }
}
